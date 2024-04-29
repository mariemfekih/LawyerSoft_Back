package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.*;
import com.example.gestion_user.entities.enums.Role;
import com.example.gestion_user.entities.enums.UserRole;
import com.example.gestion_user.exceptions.*;
import com.example.gestion_user.models.request.UserDto;
import com.example.gestion_user.repositories.CaseRepository;
import com.example.gestion_user.repositories.UserRepository;
//import com.example.gestion_user.security.LoginAttemptService;
import com.example.gestion_user.security.LoginAttemptService;
import com.example.gestion_user.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.gestion_user.constants.FileConstant.*;
import static com.example.gestion_user.constants.UserImplConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.*;


@Service("userDetailsService")
/*@Transactional
@Qualifier("userDetailsService")*/
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaseRepository caseRepository;
   private Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            LOGGER.error(NO_USER_FOUND_BY_EMAIL + email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(FOUND_USER_BY_EMAIL + email);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String cin, String email, String password, Date birthDate, String city, Boolean gender, UserRole role) throws UserNotFoundException, EmailExistException, UsernameExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setCin(cin);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(UserRole.LAWYER);
        user.setBirthDate(birthDate);
        user.setCity(city);
        user.setGender(gender);
        user.setJoinDate(new Date());
        user.setActive(true);
        user.setNotLocked(true);
        user.setAuthorities(Role.ROLE_MANAGER.getAuthorities());
        user.setProfileImage(getTemporaryProfileImage(username));
        userRepository.save(user);
        LOGGER.info("New user registered: " + username);
        return user;
    }

    @Override
    public User addUser(UserDto u){
        User user= new User();
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());
        user.setCity(u.getCity());
        user.setCin(u.getCin());
        user.setRole(u.getRole());
        user.setGender(u.getGender());
        user.setActive(true);
        user.setNotLocked(true);
        try {
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create user: " + ex.getMessage());
        }
    }
    @Override
    public User updateUser(Long id, UserDto updatedUserDto) {
        // Find the existing User entity by ID
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        // Update the fields of the existing User entity with values from the DTO
        existingUser.setFirstName(updatedUserDto.getFirstName());
        existingUser.setLastName(updatedUserDto.getLastName());
        existingUser.setUsername(updatedUserDto.getUsername());
        existingUser.setEmail(updatedUserDto.getEmail());
        existingUser.setCity(updatedUserDto.getCity());
        existingUser.setCin(updatedUserDto.getCin());
        existingUser.setRole(updatedUserDto.getRole());
        existingUser.setGender(updatedUserDto.getGender());
                try {
            return userRepository.save(existingUser);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update user with id: " + id + ". " + ex.getMessage());
        }
    }


    @Override
    public User addUser(String firstName, String lastName, String username,String cin, String email, String password, UserRole role,Date birthDate,String city,Boolean gender, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setCin(cin);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setBirthDate(birthDate);
        user.setCity(city);
        user.setGender(gender);
        user.setRole(UserRole.LAWYER);
        user.setJoinDate(new Date());
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
     //   user.setAuthorities((getRoleEnumName(role).getAuthorities()));
        user.setProfileImage(getTemporaryProfileImage(username));
        userRepository.save(user);
        saveProfileImage(user,profileImage);
        LOGGER.info("New user added: " + username);
        return user;
    }

    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail,String newCin,UserRole newRole,Date newbirthDate,String newCity,Boolean newGender, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException {
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setCin(newCin);
        currentUser.setRole(UserRole.LAWYER);
        currentUser.setBirthDate(newbirthDate);
        currentUser.setCity(newCity);
        currentUser.setGender(newGender);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
//        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);    }

    @Override
    public void deleteUser(String email) throws  UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        userRepository.delete(user);
    }
    public void deleteUser(Long idUser) {
        userRepository.deleteById(idUser);
    }


    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if(!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if(!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImage(setProfileImage(user.getUsername()));
            userRepository.save(user);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImage(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String getTemporaryProfileImage(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
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

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = getUserByUsername(newUsername);
        User userByNewEmail = getUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = getUserByUsername(currentUsername);
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


    @Override
    public Case saveCase(Case case1){
        return caseRepository.save(case1);
    }

    @Override
    public void addUserToCase(String email, String title){
      Case case1 =caseRepository.findByTitle(title);
      User user=userRepository.findByEmail(email);
      case1.getUsers().add(user);
    }

}
