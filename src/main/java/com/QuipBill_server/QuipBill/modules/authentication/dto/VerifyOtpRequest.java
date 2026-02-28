package com.QuipBill_server.QuipBill.modules.authentication.dto;

public class VerifyOtpRequest {

    private String username;
    private String otp;

    public VerifyOtpRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}