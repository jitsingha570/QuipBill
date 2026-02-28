package com.QuipBill_server.QuipBill.modules.authentication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Inject from application.properties
    @Value("${spring.mail.from}")
    private String fromEmail;

    // Email format validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Constructor Injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {

        // Validate null
        if (toEmail == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        // Trim spaces
        toEmail = toEmail.trim();

        // Validate format
        if (!EMAIL_PATTERN.matcher(toEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + toEmail);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);   // comes from application.properties
            message.setTo(toEmail);
            message.setSubject("QuipBill - Email Verification OTP");

            message.setText(
                    "Dear Shop Owner,\n\n" +
                    "Your OTP for QuipBill email verification is: " + otp + "\n\n" +
                    "This OTP is valid for 5 minutes.\n\n" +
                    "⚠ Do not share this OTP with anyone.\n\n" +
                    "Regards,\n" +
                    "QuipBill Team"
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }
}