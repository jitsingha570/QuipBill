package com.QuipBill_server.QuipBill.modules.authentication.controller;

import com.QuipBill_server.QuipBill.modules.authentication.dto.*; //"*" means import all classes in the dto package
import com.QuipBill_server.QuipBill.modules.authentication.service.ShopAuthService;

import org.springframework.http.HttpStatus; 
// HttpStatus is used to specify the HTTP status code in the response 
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        String message = shopAuthService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, message));
    }
    //decoding line "public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {" means that this method is an endpoint for handling HTTP POST requests to the "/register" URL. It takes a RegisterRequest object as input, which is expected to be in the body of the request. The @Valid annotation indicates that the input should be validated according to the constraints defined in the RegisterRequest class. The method returns a ResponseEntity containing an ApiResponse object, which includes a success flag and a message. The response status is set to HttpStatus.CREATED (201) to indicate that a new resource has been created as a result of the registration process.
//
    // ===============================
    // VERIFY OTP
    // ===============================
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        AuthResponse response = shopAuthService.verifyOtp(
                request.getUsername(),
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
}