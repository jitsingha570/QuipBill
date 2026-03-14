package com.QuipBill_server.QuipBill.modules.authentication.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class EmailService {

    @Value("${app.brevo.api-key}")
    private String apiKey;

    @Value("${app.email.sender-email}")
    private String senderEmail;

    @Value("${app.email.sender-name}")
    private String senderName;

    // Email format validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void sendOtpEmail(String toEmail, String otp) {

        // Validate null
        if (toEmail == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email cannot be null");
        }

        toEmail = toEmail.trim();

        // Validate format
        if (!EMAIL_PATTERN.matcher(toEmail).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid email format: " + toEmail);
        }

        try {

            String url = "https://api.brevo.com/v3/smtp/email";

            Map<String, Object> body = new HashMap<>();

            // Sender
            Map<String, String> sender = new HashMap<>();
            sender.put("email", senderEmail);
            sender.put("name", senderName);
            body.put("sender", sender);

            // Recipient
            List<Map<String, String>> to = new ArrayList<>();
            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", toEmail);
            to.add(recipient);
            body.put("to", to);

            // Email subject
            body.put("subject", "QuipBill - Email Verification OTP");

            // Email content
            body.put("htmlContent",
                    "<p>Dear Shop Owner,</p>" +
                    "<p>Your OTP for <b>QuipBill email verification</b> is:</p>" +
                    "<h2>" + otp + "</h2>" +
                    "<p>This OTP is valid for <b>5 minutes</b>.</p>" +
                    "<p>⚠ Do not share this OTP with anyone.</p>" +
                    "<br>" +
                    "<p>Regards,<br>QuipBill Team</p>"
            );

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send OTP email");
            }

        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send OTP email: " + e.getMessage());
        }
    }
}
