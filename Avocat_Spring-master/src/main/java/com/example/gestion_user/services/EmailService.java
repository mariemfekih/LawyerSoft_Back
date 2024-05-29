package com.example.gestion_user.services;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {
    String sendMail(
          //  MultipartFile[] file,
            String to, String[] cc, String subject, String body);
    void sendEmailCalendar(Appointment a, User user) throws MessagingException;
    String loadTemplate(String templatePath) throws IOException;
//    void sendMailTemplate(String to, String subject, String body)throws MessagingException;
void sendMailTemplate(String to, String subject, String templateContent, Map<String, String> placeholders) throws MessagingException;
    void sendOtpEmail(String to, String templateContent,String otp ,Map<String, String> placeholders) throws MessagingException;
}
