package com.QuipBill_server.QuipBill.modules.authentication.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.QuipBill_server.QuipBill.modules.authentication.dto.*;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import com.QuipBill_server.QuipBill.modules.authentication.repository.RoleRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Role;
import com.QuipBill_server.QuipBill.modules.authentication.entity.RoleName;
import com.QuipBill_server.QuipBill.modules.authentication.security.JwtUtil;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;

import java.util.Set;
import java.util.Optional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShopAuthService {

    private final ShopRepository shopRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    private final Clock clock = Clock.systemUTC();

    public ShopAuthService(ShopRepository shopRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           EmailService emailService) {
        this.shopRepository = shopRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // ===============================
    // REGISTER (Send OTP)
    // ===============================
    public RegisterResponse register(RegisterRequest request) {

        validateRegisterRequest(request);
        String email = normalizeEmail(request.getEmail(), "Email is required");

        Optional<Shop> existingShopOpt = shopRepository.findByEmail(email);

        if (existingShopOpt.isPresent()) {
            Shop existingShop = existingShopOpt.get();

            if (Boolean.TRUE.equals(existingShop.getVerified())) {
                throw new CustomException("Email already registered");
            }

            applyRegisterDetails(existingShop, request);

            String otp = generateOtp();
            existingShop.setOtp(otp);
            existingShop.setOtpExpiry(LocalDateTime.now(clock).plusMinutes(5));

            shopRepository.save(existingShop);
            emailService.sendOtpEmail(existingShop.getEmail(), otp);

            return new RegisterResponse(true, "OTP resent", existingShop.getEmail());
        }

        Shop shop = new Shop();
        shop.setEmail(email);
        shop.setVerified(false);

        applyRegisterDetails(shop, request);

        Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new CustomException("Role not found"));

        shop.addRole(role);

        String otp = generateOtp();
        shop.setOtp(otp);
        shop.setOtpExpiry(LocalDateTime.now(clock).plusMinutes(5));

        shopRepository.save(shop);
        emailService.sendOtpEmail(shop.getEmail(), otp);

        return new RegisterResponse(true, "OTP sent", shop.getEmail());
    }

    // ===============================
    // VERIFY OTP
    // ===============================
    public VerifyOtpResponse verifyOtp(String emailInput, String otp) {

        String email = normalizeEmail(emailInput, "Email is required");

        Shop shop = shopRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        if (!otp.equals(shop.getOtp())) {
            throw new CustomException("Invalid OTP");
        }

        if (shop.getOtpExpiry() == null ||
                shop.getOtpExpiry().isBefore(LocalDateTime.now(clock))) {
            throw new CustomException("OTP expired");
        }

        shop.setVerified(true);
        shop.setOtp(null);
        shop.setOtpExpiry(null);
        shopRepository.save(shop);

     
        return new VerifyOtpResponse(
                true,
                "OTP verified",
                shop.getEmail(),
                getRoleNames(shop)
        );
    }

    // ===============================
    // LOGIN
    // ===============================
    public AuthResponse login(LoginRequest request) {

        String email = normalizeEmail(request.getEmail(), "Email required");

        Shop shop = shopRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), shop.getPassword())) {
            throw new CustomException("Invalid credentials");
        }

        if (!Boolean.TRUE.equals(shop.getVerified())) {
            throw new CustomException("Verify account first");
        }

        // 🔥 NEW TOKEN SYSTEM
        String accessToken = jwtUtil.generateAccessToken(shop.getId().toString());
        String refreshToken = jwtUtil.generateRefreshToken(shop.getId().toString());

        return new AuthResponse(
                accessToken,
                refreshToken,
                shop.getId(),
                shop.getEmail(),
                getRoleNames(shop)
        );
    }

    // ===============================
    // REFRESH TOKEN (NEW)
    // ===============================
    public String refreshAccessToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken, true)) {
            throw new CustomException("Invalid refresh token");
        }

        String userId = jwtUtil.extractUsername(refreshToken, true);

        return jwtUtil.generateAccessToken(userId);
    }

    // ===============================
    // LOGOUT (OPTIONAL)
    // ===============================
    public void logout(String refreshToken) {
        // Future: delete token from DB
    }

    // ===============================
    // PROFILE
    // ===============================
    public ShopProfileResponse getProfile(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException("User not found"));

        return mapToProfile(shop);
    }

    public ShopProfileResponse updateProfile(Long shopId, ShopProfileUpdateRequest request) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException("User not found"));

        if (request.getShopName() != null)
            shop.setShopName(request.getShopName());

        if (request.getMobileNumber() != null)
            shop.setMobileNumber(request.getMobileNumber());

        if (request.getAddress() != null)
            shop.setAddress(request.getAddress());

        if (request.getPincode() != null)
            shop.setPincode(request.getPincode());

        return mapToProfile(shopRepository.save(shop));
    }

    // ===============================
    // UTIL
    // ===============================
    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private String normalizeEmail(String email, String msg) {
        if (email == null || email.isEmpty()) throw new CustomException(msg);
        return email.toLowerCase().trim();
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request == null) throw new CustomException("Invalid request");
    }

    private void applyRegisterDetails(Shop shop, RegisterRequest request) {
        if (request.getPassword() != null)
            shop.setPassword(passwordEncoder.encode(request.getPassword()));
        shop.setShopName(request.getShopName());
    }

    private Set<String> getRoleNames(Shop shop) {
        return shop.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    private ShopProfileResponse mapToProfile(Shop shop) {
        return new ShopProfileResponse(
                shop.getId(),
                shop.getEmail(),
                shop.getMobileNumber(),
                shop.getShopName(),
                shop.getShopOwnerName(),
                shop.getAddress(),
                shop.getPincode()
        );
    }
}