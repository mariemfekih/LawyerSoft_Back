package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.entities.enums.UserRole;
import com.example.gestion_user.exceptions.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    public User getUserById(Long id);


    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User register(String firstName, String lastName, String username, String cin, String email, String password, Date birthdate, String city, Boolean gender, UserRole role) throws UserNotFoundException, EmailExistException, CinExistException, UsernameExistException;



    public User addNewUser(String firstName, String lastName, String username,String cin, String email, String password, UserRole role,Date birthDate,String city,Boolean gender, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException, NotAnImageFileException;

    /*
    UPDATE without UserRole!!!
     */
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail,String newCin,UserRole newRole,Date newbirthDate,String newCity,Boolean newGender, boolean isNonLocked, boolean isActive) throws UserNotFoundException, UsernameExistException, EmailExistException,CinExistException, IOException;

    /*
    DELETEEEE
     */
    void deleteUser(String email) throws  UserNotFoundException;
    void deleteUser(Long id) throws  UserNotFoundException;

    //public void resetPassword(String email, String newPassword) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;



    //void resetPassword(String email) throws MessagingException, EmailNotFoundException;



//user+case
    Case saveCase (Case case1);
    void addUserToCase(String email,String title);






}
