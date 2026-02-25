package com.QuipBill_server.QuipBill.modules.authentication.controller;

import com.QuipBill_server.QuipBill.modules.authentication.dto.*;
import com.QuipBill_server.QuipBill.modules.authentication.service.ShopAuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ShopAuthController {

    private final ShopAuthService shopAuthService;

    public ShopAuthController(ShopAuthService shopAuthService) {
        this.shopAuthService = shopAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(shopAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(shopAuthService.login(request));
    }
}