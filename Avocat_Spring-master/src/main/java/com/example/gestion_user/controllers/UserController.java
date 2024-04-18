package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.UserPrincipal;
import com.example.gestion_user.entities.enums.UserRole;
import com.example.gestion_user.exceptions.*;
import com.example.gestion_user.repositories.UserRepository;
//import com.example.gestion_user.security.JWTTokenProvider;
import com.example.gestion_user.security.JWTTokenProvider;
import com.example.gestion_user.services.UserService;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.example.gestion_user.constants.FileConstant.*;
//import static com.example.gestion_user.security.SecurityConstants.JWT_TOKEN_HEADER;
import static com.example.gestion_user.security.SecurityConstants.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping( "/user")
@CrossOrigin("http://localhost:4200")

public class UserController  {

    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }
    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }


    /*
    REGISTERRRRR
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException, CinExistException,MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getCin(),user.getEmail(), user.getPassword(),user.getBirthDate(),user.getCity(),user.getGender(),user.getRole());

        // After successful registration, create a UserPrincipal
        UserPrincipal userPrincipal = new UserPrincipal(newUser);

        // Generate JWT token
        String token = jwtTokenProvider.generateJwtToken(userPrincipal);

        // Prepare response with user details and token
        Map<String, Object> response = new HashMap<>();
        response.put("user", newUser);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

/*
    LOGIN
 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        authenticate(user.getEmail(), user.getPassword());

        User loginUser = userService.getUserByEmail(user.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        // Generate JWT token
        String token = jwtTokenProvider.generateJwtToken(userPrincipal);

        // Return token in the response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }


   private HttpHeaders getJwtHeader(UserPrincipal user) {
       HttpHeaders headers = new HttpHeaders();
       headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
       return headers;
   }

   @PostMapping("/addUser")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("cin") Long cin,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password,
                                           @RequestParam("role") UserRole role,
                                           @RequestParam("birthDate") Date birthDate,
                                           @RequestParam("city") String city,
                                           @RequestParam("gender") String gender,
                                           @RequestParam("isNonLocked") String isNonLocked,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException, NotAnImageFileException {
        User newUser = userService.addNewUser(firstName, lastName,username,cin, email,password, role,birthDate,city,Boolean.parseBoolean(gender),
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(newUser, OK);
   }

    @PostMapping("/updateUser")
    public ResponseEntity<User> update(@RequestParam("currentUsername") String currentUsername,
                                       @RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName,
                                       @RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       @RequestParam("cin") Long cin,
                                       @RequestParam("role") UserRole role,
                                       @RequestParam("birthDate") Date birthDate,
                                       @RequestParam("city") String city,
                                       @RequestParam("gender") String gender,
                                       @RequestParam("isActive") String isActive,
                                       @RequestParam("isNonLocked") String isNonLocked) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, CinExistException {
        User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username,email, cin,role,birthDate,city,Boolean.parseBoolean(gender), Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(updatedUser, OK);
    }

    @DeleteMapping("/deleteUser/{email}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("email") String email) throws  UserNotFoundException {
        userService.deleteUser(email);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }
    @DeleteMapping("/deleteUser/{idUser}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("idUser") Integer idUser) throws UserNotFoundException {
        userService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }
    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username, @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }

 @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }



}
