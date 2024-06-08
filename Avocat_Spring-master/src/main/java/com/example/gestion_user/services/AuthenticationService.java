package com.example.gestion_user.services;

import com.example.gestion_user.entities.User;
import com.example.gestion_user.exceptions.CinExistException;
import com.example.gestion_user.exceptions.EmailExistException;
import com.example.gestion_user.exceptions.UserNotFoundException;
import com.example.gestion_user.exceptions.UsernameExistException;
import com.example.gestion_user.models.request.UserDto;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AuthenticationService {
    User register(UserDto userDto) throws UserNotFoundException, EmailExistException, UsernameExistException, CinExistException, IOException, MessagingException;
    User registerManager(UserDto userDto) throws UserNotFoundException, EmailExistException, UsernameExistException, CinExistException, IOException, MessagingException;


    /*Controle de saisi*/
    String getPasswordStrength(String password);
    boolean isCinValid(String cin);
    boolean isEmailValid(String email);
    String generateUniqueUsername(String firstName, String lastName);
}
