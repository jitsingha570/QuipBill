package com.QuipBill_server.QuipBill.modules.authentication.dto;

import java.util.Set;

public class VerifyOtpResponse {

    private boolean success;
    private String message;
    //private String token;
    private String email;
    private Set<String> roles;

    public VerifyOtpResponse(boolean success, String message, String email, Set<String> roles) {
        this.success = success;
        this.message = message;
       // this.token = token;
        this.email = email;
        this.roles = roles;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    /*public String getToken() {
        return token;
    }*/

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
