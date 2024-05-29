package com.example.gestion_user.controllers;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.services.EmailService;
import com.example.gestion_user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/mail")
public class EmailSendController {
    private EmailService emailService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);

    public EmailSendController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendMail(
         //   @RequestParam(value = "file", required = false) MultipartFile[] file,
            String to, String[] cc, String subject, String body) {
        return emailService.sendMail(
            //    file,
                to, cc, subject, body);
    }
////////////////////////////////////////////////////////////////////////
@PostMapping("/sendTemplate/{idUser}")
public ResponseEntity<?> sendEmailTemplate(
        @RequestParam("to") String to,
        @RequestParam("subject") String subject,
        @RequestParam("templatePath") String templatePath,
        @PathVariable("idUser") Long idUser) {
    try {
        Optional<User> optionalUser = userService.getUserByid(idUser);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = optionalUser.get();

        // Load template content
        String templateContent = emailService.loadTemplate(templatePath);

        // Replace placeholders in the template content
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("firstName", user.getFirstName());
        placeholders.put("lastName", user.getLastName());

        // Send email
        emailService.sendMailTemplate(to, subject, templateContent, placeholders);

        return ResponseEntity.ok("Email sent successfully");
    } catch (IOException e) {
        // Log the exception and return an appropriate response
        logger.error("Failed to load email template: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to load email template: " + e.getMessage());
    } catch (MessagingException e) {
        // Log the exception and return an appropriate response
        logger.error("Failed to send email: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + e.getMessage());
    }
}
/////////////////////


}
