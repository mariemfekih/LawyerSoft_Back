package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.UserPrincipal;
import com.example.gestion_user.entities.enums.UserRole;
import com.example.gestion_user.exceptions.*;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.UserDto;
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


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto u) {
        User addedUser = userService.addUser(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    /*
     * Update Case
     */
    @PutMapping("/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable Long idUser, @RequestBody UserDto updatedUserDto) {
        User existingUser = userService.getUserById(idUser);

        if (existingUser == null) {
            // If the User doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        User updatedUser = userService.updateUser(idUser, updatedUserDto);
        // Return the updated User with a 200 OK status
        return ResponseEntity.ok(updatedUser);
    }





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

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }


    /*
    REGISTERRRRR
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) throws UserNotFoundException, UsernameExistException, EmailExistException, CinExistException,MessagingException {
        // Check password strength
        String passwordStrength = userService.getPasswordStrength(userDto.getPassword());
        if ("faible".equals(passwordStrength)) {
            return ResponseEntity.badRequest().body("Le mot de passe ne respecte pas les critères de validation.");
        }
        // Check CIN validity
        if (!userService.isCinValid(userDto.getCin())) {
            return ResponseEntity.badRequest().body("Le CIN n'est pas valide.");
        }
        // Check email validity
        if (!userService.isEmailValid(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("L'adresse e-mail n'est pas valide.");
        }
        // Generate unique username
        String username = userService.generateUniqueUsername(userDto.getFirstName(), userDto.getLastName());

        // Set isActive to false
        userDto.setActive(false);

        User newUser = userService.register(userDto);

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
public ResponseEntity<?> login(@RequestBody UserDto userDto) {
    authenticate(userDto.getEmail(), userDto.getPassword());

    User loginUser = userService.getUserByEmail(userDto.getEmail());
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





    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/deleteUser/{email}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("email") String email) throws  UserNotFoundException {
        userService.deleteUser(email);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idUser}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("idUser") Long idUser) throws UserNotFoundException {
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
