package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.enums.Role;
import com.example.gestion_user.exceptions.CinExistException;
import com.example.gestion_user.exceptions.EmailExistException;
import com.example.gestion_user.exceptions.UserNotFoundException;
import com.example.gestion_user.exceptions.UsernameExistException;
import com.example.gestion_user.models.request.UserDto;
import com.example.gestion_user.repositories.UserRepository;
import com.example.gestion_user.security.LoginAttemptService;
import com.example.gestion_user.services.AuthenticationService;
import com.example.gestion_user.services.EmailService;
import com.example.gestion_user.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.gestion_user.constants.UserImplConstant.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Override
    public User register(UserDto userDto) throws UserNotFoundException, EmailExistException, UsernameExistException, CinExistException, IOException, MessagingException {
        // Check for duplicate CIN
        validateCin(userDto.getCin());

        // Ensure email and username are unique
        validateNewUsernameAndEmail(EMPTY, generateUniqueUsername(userDto.getFirstName(), userDto.getLastName()), userDto.getEmail());

        User user = new User();
        String encodedPassword = encodePassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(generateUniqueUsername(userDto.getFirstName(), userDto.getLastName()));
        user.setCin(userDto.getCin());
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(userDto.getRole());
        user.setBirthDate(userDto.getBirthDate());
        user.setCity(userDto.getCity());
        user.setGender(userDto.getGender());
        user.setJoinDate(new Date());
        user.setActive(false);
        user.setNotLocked(false);
        user.setLawyerId(null);
        user.setAuthorities(Role.ROLE_MANAGER.getAuthorities());
        user.setProfileImage(userService.getTemporaryProfileImage(user.getUsername()));
        userRepository.save(user);
///////////////////////Envoi du mail merci pour inscri
        String templateContent = emailService.loadTemplate("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\calendar\\afterSignup.html");

        // Replace placeholders in the template content
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("firstName", user.getFirstName());
        placeholders.put("lastName", user.getLastName());

        // Send email
        emailService.sendMailTemplate(user.getEmail(), "Merci pour votre inscription ! Votre compte sera bientôt activé", templateContent, placeholders);
        /////////////////////////
        LOGGER.info("New user registered: " + user.getUsername());
        return user;
    }
    @Override
    public User registerManager(UserDto userDto) throws UserNotFoundException, EmailExistException, UsernameExistException, CinExistException, IOException, MessagingException {
        // Check for duplicate CIN
        validateCin(userDto.getCin());

        // Ensure email and username are unique
        validateNewUsernameAndEmail(EMPTY, generateUniqueUsername(userDto.getFirstName(), userDto.getLastName()), userDto.getEmail());

        User user = new User();
        String encodedPassword = encodePassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(generateUniqueUsername(userDto.getFirstName(), userDto.getLastName()));
        user.setCin(userDto.getCin());
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(userDto.getRole());
        user.setBirthDate(userDto.getBirthDate());
        user.setCity(userDto.getCity());
        user.setGender(userDto.getGender());
        user.setJoinDate(new Date());
        user.setActive(false);
        user.setNotLocked(false);
        user.setLawyerId(userDto.getLawyerId());
        user.setAuthorities(Role.ROLE_MANAGER.getAuthorities());
        user.setProfileImage(userService.getTemporaryProfileImage(user.getUsername()));
        userRepository.save(user);
///////////////////////Envoi du mail merci pour inscri
        String templateContent = emailService.loadTemplate("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\calendar\\afterSignup.html");

        // Replace placeholders in the template content
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("firstName", user.getFirstName());
        placeholders.put("lastName", user.getLastName());

        // Send email
        emailService.sendMailTemplate(user.getEmail(), "Merci pour votre inscription ! Votre compte sera bientôt activé", templateContent, placeholders);
        /////////////////////////
        LOGGER.info("New user registered: " + user.getUsername());
        return user;
    }




    /*Controle de saisi*/


    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.removeUserFromLoginAttemptCache(user.getUsername());
        }
    }
    public String getPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return "faible";
        } else if (password.length() < 8) {
            return "faible";
        } else if (!password.matches(".*\\d.*")) {
            return "faible";
        } else if (!password.matches(".*[a-z].*")) {
            return "faible";
        } else if (!password.matches(".*[A-Z].*")) {
            return "faible";
        } else if (!password.matches(".*[!@#$%^&*()-_=+\\\\|\\[{\\]};:'\",<.>/?].*")) {
            return "faible";
        } else if (password.length() < 12 ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*[!@#$%^&*()-_=+\\\\|\\[{\\]};:'\",<.>/?].*")) {
            return "moyenne";
        } else {
            return "fort";
        }
    }
    private void validateCin(String cin) throws CinExistException {
        if (userRepository.existsByCin(cin)) {
            throw new CinExistException("CIN already exists");
        }
    }
    public boolean isCinValid(String cin) {
        if (cin == null || !cin.matches("[0-1]\\d{7}")) {
            return false; // CIN must be 8 numbers starting with 0 or 1
        }
        return true; // CIN is valid
    }
    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = userService.getUserByUsername(newUsername);
        User userByNewEmail = userService.getUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = userService.getUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    public boolean isEmailValid(String email) {
        if (email == null || email.isEmpty()) {
            return false; // Email cannot be null or empty
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName.toLowerCase() + "." + lastName.toLowerCase();
        String username = baseUsername;
        int counter = 1;
        // Add logging to see the username generation process
        System.out.println("Generating unique username...");
        System.out.println("Base username: " + baseUsername);
        // Check if the base username is already taken
        while (userService.getUserByUsername(username) != null) {
            // Append the counter to the base username to make it unique
            username = baseUsername + counter;
            counter++;
            System.out.println("Username already exists. Trying: " + username);
        }

        System.out.println("Generated unique username: " + username);
        return username;
    }
}
