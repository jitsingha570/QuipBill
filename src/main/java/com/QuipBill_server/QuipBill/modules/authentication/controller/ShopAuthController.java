package com.QuipBill_server.QuipBill.modules.authentication.controller;

import com.QuipBill_server.QuipBill.modules.authentication.dto.*;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;
import com.QuipBill_server.QuipBill.modules.authentication.service.ShopAuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ShopAuthController {

    private final ShopAuthService shopAuthService;

    public ShopAuthController(ShopAuthService shopAuthService) {
        this.shopAuthService = shopAuthService;
    }

    // ===============================
    // REGISTER → Send OTP
    // ===============================
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = shopAuthService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===============================
    // VERIFY OTP
    // ===============================
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        VerifyOtpResponse response = shopAuthService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(response);
    }

    // ===============================
    // LOGIN (UPDATED 🔥)
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        // 🔥 Now returns access + refresh token
        AuthResponse response = shopAuthService.login(request);

        return ResponseEntity.ok(response);
    }

    // ===============================
    // REFRESH TOKEN (NEW 🔥)
    // ===============================
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Refresh token required"));
        }

        String newAccessToken = shopAuthService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken
        ));
    }

    // ===============================
    // LOGOUT (NEW 🔥)
    // ===============================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        shopAuthService.logout(refreshToken);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ===============================
    // PROFILE
    // ===============================
    @GetMapping("/profile")
    public ResponseEntity<ShopProfileResponse> getProfile(Authentication authentication) {
        Long shopId = getShopId(authentication);
        return ResponseEntity.ok(shopAuthService.getProfile(shopId));
    }

    @PutMapping("/profile")
    public ResponseEntity<ShopProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody ShopProfileUpdateRequest request) {
        Long shopId = getShopId(authentication);
        return ResponseEntity.ok(shopAuthService.updateProfile(shopId, request));
    }

    // ===============================
    // AUTH HELPER
    // ===============================
    private Long getShopId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new CustomException("Unauthorized");
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new CustomException("Unauthorized");
        }
    }
}