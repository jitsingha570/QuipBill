package com.QuipBill_server.QuipBill.modules.authentication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;

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
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email cannot be null");
        }

        // Trim spaces
        toEmail = toEmail.trim();

        // Validate format
        if (!EMAIL_PATTERN.matcher(toEmail).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid email format: " + toEmail);
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
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send OTP email");
        }
    }
}