package com.mindhub.homebanking.services;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendConfirmationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Confirmation");
        message.setText("Please confirm your email using the following link: \n" +
                "http://localhost:8080/confirmEmail?token=" + token);
        javaMailSender.send(message);
    }


}*/
