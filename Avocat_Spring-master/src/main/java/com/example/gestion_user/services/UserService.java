package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.enums.UserRole;
import com.example.gestion_user.exceptions.*;
import com.example.gestion_user.models.request.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserService {



    public User addUser(UserDto user);
    public User updateUser(Long id, UserDto updatedUserDto);
    User register(UserDto userDto) throws UserNotFoundException, EmailExistException, UsernameExistException, CinExistException, IOException, MessagingException;
    User updateUserActiveState(Long userId, boolean newActiveState) throws Exception;

    /*
    DELETEEEE
     */
    void deleteUser(String email) throws  UserNotFoundException;
    void deleteUser(Long id) throws  UserNotFoundException;

    List<User> getUsers();

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    public User getUserById(Long id);
    Optional<User> getUserByid(Long id);

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;




    //public void resetPassword(String email, String newPassword) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;



    //void resetPassword(String email) throws MessagingException, EmailNotFoundException;



//user+case
    Case saveCase (Case case1);
   // void addUserToCase(String email,String title);


/*Controle de saisi*/
String getPasswordStrength(String password);
boolean isCinValid(String cin);
boolean isEmailValid(String email);
String generateUniqueUsername(String firstName, String lastName);

}
