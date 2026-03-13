package com.QuipBill_server.QuipBill.modules.authentication.controller;

import com.QuipBill_server.QuipBill.modules.authentication.dto.AuthResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.LoginRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.RegisterRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.RegisterResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.VerifyOtpResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.VerifyOtpRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.ShopProfileResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.ShopProfileUpdateRequest;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;
import com.QuipBill_server.QuipBill.modules.authentication.service.ShopAuthService;

import org.springframework.http.HttpStatus; 
// HttpStatus is used to specify the HTTP status code in the response 
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController  // Indicates that this class is a REST controller, which means it will handle HTTP requests and return responses in a RESTful manner
@RequestMapping("/api/auth")// Base URL for all endpoints in this controller. All endpoints will start with "/api/auth"
public class ShopAuthController {

    private final ShopAuthService shopAuthService; //decode line "private final ShopAuthService shopAuthService;" means that this class has a dependency on the ShopAuthService, which is a service class that contains the business logic for authentication. The "final" keyword indicates that this variable cannot be reassigned after it has been initialized, ensuring that the controller always uses the same instance of the service. The service will be injected into the controller through the constructor, allowing the controller to call methods on the service to perform authentication-related operations.  shopAuthService is a variable that holds an instance of the ShopAuthService class, which is responsible for handling the business logic related to authentication for shops. This service will be used by the controller to perform actions such as registering a new shop, verifying OTPs, and logging in.

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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    //decoding line "public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {" means that this method is an endpoint for handling HTTP POST requests to the "/register" URL. It takes a RegisterRequest object as input, which is expected to be in the body of the request. The @Valid annotation indicates that the input should be validated according to the constraints defined in the RegisterRequest class. The method returns a ResponseEntity containing an ApiResponse object, which includes a success flag and a message. The response status is set to HttpStatus.CREATED (201) to indicate that a new resource has been created as a result of the registration process.
//
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
    // LOGIN
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = shopAuthService.login(request);

        return ResponseEntity.ok(response);
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
