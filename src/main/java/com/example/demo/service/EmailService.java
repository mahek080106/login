package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("OTP for Registration");

        message.setText(
                "Hello,\n\n" +
                "Your OTP for registration is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n\n" +
                "Thank you."
        );

        mailSender.send(message);
    }
}