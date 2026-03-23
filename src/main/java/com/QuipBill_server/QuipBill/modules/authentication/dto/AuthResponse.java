package com.QuipBill_server.QuipBill.modules.authentication.dto;

import java.util.Set;

public class AuthResponse {

    private String accessToken;   // 🔥 NEW
    private String refreshToken;  // 🔥 NEW
    private Long shopId;
    private String username;
    private Set<String> roles;

    // 🔥 UPDATED CONSTRUCTOR
    public AuthResponse(String accessToken,
                        String refreshToken,
                        Long shopId,
                        String username,
                        Set<String> roles) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.shopId = shopId;
        this.username = username;
        this.roles = roles;
    }

    // ===============================
    // GETTERS
    // ===============================

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
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