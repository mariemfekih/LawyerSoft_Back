package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Random;


@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;
    private  TemplateEngine templateEngine;

    @Override
    public String sendMail(String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

       /*     for (int i = 0; i < file.length; i++) {
                mimeMessageHelper.addAttachment(
                        file[i].getOriginalFilename(),
                        new ByteArrayResource(file[i].getBytes()));
            }
*/
            javaMailSender.send(mimeMessage);

            return "mail send";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    //Send template email

    public String replacePlaceholders(String templateContent, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            templateContent = templateContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return templateContent;
    }
@Override
    public String loadTemplate(String templatePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(templatePath)));
    }
@Override
public void sendMailTemplate(String to, String subject, String templateContent, Map<String, String> placeholders) throws MessagingException {
    String processedContent = replacePlaceholders(templateContent, placeholders);
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(to);
    helper.setSubject(subject);

    // Set HTML content and embed image
   // processedContent = processedContent.replace("src/main/resources/images/logo_template.png", "cid:logoImage");
    helper.setText(processedContent, true); // Set to true to indicate the content is HTML

    // Add the image as an inline resource
//    Resource imageResource = new ClassPathResource("images/logo_template.png");
//    helper.addInline("logoImage", imageResource);

    javaMailSender.send(message);
}






/*
Calendar
* */
    public void sendEmailCalendar(Appointment appointment, User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Appointment Invitation");

        Context context = new Context();
        context.setVariable("appointment", appointment);
        context.setVariable("user", user);
        System.out.println(appointment.getType());
        String html = templateEngine.process("C:/Users/marie/Documents/Github/LawyerSoft_Back/Avocat_Spring-master/src/main/resources/calendar/mail_template.html", context);

        helper.setText(html, true);

        javaMailSender.send(message);
    }

    @Override
    public void sendOtpEmail(String to, String templateContent,String otp ,Map<String, String> placeholders) throws MessagingException {
        String processedContent = replacePlaceholders(templateContent, placeholders);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Verifier Otp");

        helper.setText(processedContent, true); // Set to true to indicate the content is HTML

        javaMailSender.send(message);
    }}
