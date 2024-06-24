package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.UserPrincipal;
import com.example.gestion_user.exceptions.*;
import com.example.gestion_user.models.request.UserDto;
import com.example.gestion_user.security.JWTTokenProvider;
import com.example.gestion_user.services.AuthenticationService;
import com.example.gestion_user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.gestion_user.security.SecurityConstants.JWT_TOKEN_HEADER;

@RestController
@RequestMapping( "/auth")
@CrossOrigin("http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto, @RequestParam("profileImageFile") MultipartFile profileImageFile) throws UserNotFoundException, UsernameExistException, EmailExistException, CinExistException, MessagingException {
        // Check password strength
        String passwordStrength = authenticationService.getPasswordStrength(userDto.getPassword());
        if ("faible".equals(passwordStrength)) {
            return ResponseEntity.badRequest().body("Le mot de passe ne respecte pas les critères de validation.");
        }
        // Check CIN validity
        if (!authenticationService.isCinValid(userDto.getCin())) {
            return ResponseEntity.badRequest().body("Le CIN n'est pas valide.");
        }
        // Check email validity
        if (!authenticationService.isEmailValid(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("L'adresse e-mail n'est pas valide.");
        }
        try {
            User newUser = authenticationService.register(userDto,profileImageFile);

            // After successful registration, create a UserPrincipal
            UserPrincipal userPrincipal = new UserPrincipal(newUser);

            // Generate JWT token
            String token = jwtTokenProvider.generateJwtToken(userPrincipal);

            // Prepare response with user details and token
            Map<String, Object> response = new HashMap<>();
            response.put("user", newUser);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (CinExistException e) {
            return ResponseEntity.badRequest().body("CIN already exists.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/registerManager")
    public ResponseEntity<?> registerManager(@RequestBody UserDto userDto) throws UserNotFoundException, UsernameExistException, EmailExistException, CinExistException, MessagingException {
        // Check password strength
        String passwordStrength = authenticationService.getPasswordStrength(userDto.getPassword());
        if ("faible".equals(passwordStrength)) {
            return ResponseEntity.badRequest().body("Le mot de passe ne respecte pas les critères de validation.");
        }
        // Check CIN validity
        if (!authenticationService.isCinValid(userDto.getCin())) {
            return ResponseEntity.badRequest().body("Le CIN n'est pas valide.");
        }
        // Check email validity
        if (!authenticationService.isEmailValid(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("L'adresse e-mail n'est pas valide.");
        }
        try {
            User newUser = authenticationService.registerManager(userDto);

            // After successful registration, create a UserPrincipal
            UserPrincipal userPrincipal = new UserPrincipal(newUser);

            // Generate JWT token
            String token = jwtTokenProvider.generateJwtToken(userPrincipal);

            // Prepare response with user details and token
            Map<String, Object> response = new HashMap<>();
            response.put("user", newUser);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (CinExistException e) {
            return ResponseEntity.badRequest().body("CIN already exists.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", loginUser);

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


}
