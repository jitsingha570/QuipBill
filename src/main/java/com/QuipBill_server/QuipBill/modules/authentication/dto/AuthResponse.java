package com.QuipBill_server.QuipBill.modules.authentication.dto;
//this file use for send response to client after login or register or verify otp
import java.util.Set;

public class AuthResponse {

    private String token;
    private Long shopId;
    private String username;
    
    private Set<String> roles;

    public AuthResponse(String token, Long shopId, String username, Set<String> roles) {
        this.token = token;
        this.shopId = shopId;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getUsername() {
        return username;
    }



    public Set<String> getRoles() {
        return roles;
    }
}
