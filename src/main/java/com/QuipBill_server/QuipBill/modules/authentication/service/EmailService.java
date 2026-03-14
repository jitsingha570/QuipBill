package com.QuipBill_server.QuipBill.modules.authentication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${EMAIL_SENDER_EMAIL}")
    private String senderEmail;

    @Value("${EMAIL_SENDER_NAME}")
    private String senderName;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendOtpEmail(String toEmail, String otp) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> sender = new HashMap<>();
        sender.put("email", senderEmail);
        sender.put("name", senderName);

        Map<String, String> to = new HashMap<>();
        to.put("email", toEmail);

        List<Map<String, String>> toList = new ArrayList<>();
        toList.add(to);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", toList);
        body.put("subject", "QuipBill OTP Verification");

        body.put(
                "htmlContent",
                "<h2>QuipBill Verification</h2>"
                + "<p>Your OTP is:</p>"
                + "<h1 style='color:green'>" + otp + "</h1>"
                + "<p>This OTP expires in 5 minutes.</p>"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                BREVO_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send OTP email via Brevo");
        }
    }
}