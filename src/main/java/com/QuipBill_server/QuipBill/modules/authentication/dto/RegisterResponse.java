package com.QuipBill_server.QuipBill.modules.authentication.dto;

public class RegisterResponse {

    private boolean success;
    private String message;
    private String email;

    public RegisterResponse(boolean success, String message, String email) {
        this.success = success;
        this.message = message;
        this.email = email;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}
