package com.QuipBill_server.QuipBill.modules.authentication.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private final EmailService emailService;

    public OtpService(EmailService emailService) {
        this.emailService = emailService;
    }

    public String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public void sendOtp(String email) {

        String otp = generateOtp();

        emailService.sendOtpEmail(email, otp);

        System.out.println("OTP Sent: " + otp);
    }
}