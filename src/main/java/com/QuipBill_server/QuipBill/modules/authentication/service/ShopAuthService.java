package com.QuipBill_server.QuipBill.modules.authentication.service;
//===== importing classes from Spring framework =====
import org.springframework.stereotype.Service;// Indicates that this class is a service component in the Spring framework, which means it contains business logic and can be injected into other components such as controllers.
import org.springframework.transaction.annotation.Transactional;// Indicates that the methods in this class should be executed within a transactional context, which means that if any method throws an exception, the transaction will be rolled back to maintain data integrity.
import org.springframework.security.crypto.password.PasswordEncoder;// Used for encoding passwords before storing them in the database and for verifying passwords during login.

//===== importing classes from our project =====
import com.QuipBill_server.QuipBill.modules.authentication.dto.RegisterRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.RegisterResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.LoginRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.AuthResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.VerifyOtpResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.ShopProfileResponse;
import com.QuipBill_server.QuipBill.modules.authentication.dto.ShopProfileUpdateRequest;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import com.QuipBill_server.QuipBill.modules.authentication.repository.RoleRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Role;
import com.QuipBill_server.QuipBill.modules.authentication.entity.RoleName;
import com.QuipBill_server.QuipBill.modules.authentication.security.JwtUtil;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;

//===== importing other necessary classes =====
import java.util.Set; // Used for handling collections of roles assigned to a shop
import java.util.Optional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors; // Used for converting collections of Role entities to a set of role names (strings) when generating the AuthResponse. This allows us to return a list of role names in the response instead of the full Role objects, which simplifies the response and avoids exposing unnecessary details about the roles.

@Service  // Indicates that this class is a service component in the Spring framework, which means it contains business logic and can be injected into other components such as controllers.
@Transactional // Indicates that the methods in this class should be executed within a transactional context, which means that if any method throws an exception, the transaction will be rolled back to maintain data integrity.

//=================================
// ShopAuthService class
//=================================
public class ShopAuthService {
//===== dependencies that will be injected into this service =====
    private final ShopRepository shopRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final Clock clock = Clock.systemUTC();
//===== constructor for dependency injection =====
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

    // 🔎 CHECK IF EMAIL ALREADY EXISTS
    if (existingShopOpt.isPresent()) {

        Shop existingShop = existingShopOpt.get();

        // ❌ If already verified → block
        if (Boolean.TRUE.equals(existingShop.getVerified())) {
            throw new CustomException("Email already registered");
        }

        // 🔁 Update details + resend OTP
        applyRegisterDetails(existingShop, request);

        String otp = generateOtp();
        existingShop.setOtp(otp);
        existingShop.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // ✅ FIXED

        shopRepository.save(existingShop);
        emailService.sendOtpEmail(existingShop.getEmail(), otp);

        return new RegisterResponse(true, "OTP resent to your email", existingShop.getEmail());
    }

    // 🆕 NEW USER
    Shop shop = new Shop();
    shop.setEmail(email);
    shop.setVerified(false);

    // ✅ APPLY ALL FIELDS (FIXED)
    applyRegisterDetails(shop, request);

    // ✅ Assign role
    Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
            .orElseThrow(() -> new CustomException("Role not found"));

    shop.addRole(role);

    // 🔐 Generate OTP
    String otp = generateOtp();
    shop.setOtp(otp);
    shop.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // ✅ FIXED

    shopRepository.save(shop);

    emailService.sendOtpEmail(shop.getEmail(), otp);

    return new RegisterResponse(true, "OTP sent to your email", shop.getEmail());
}
  // ===============================
    // VERIFY OTP
    // ===============================
 public VerifyOtpResponse verifyOtp(String emailInput, String otp) {

    String email = normalizeEmail(emailInput, "Email is required");

    if (otp == null || otp.trim().isEmpty()) {
        throw new CustomException("OTP is required");
    }

    Shop shop = shopRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("User not found"));

    if (Boolean.TRUE.equals(shop.getVerified())) {
        throw new CustomException("Account already verified");
    }

    if (shop.getOtp() == null) {
        throw new CustomException("OTP not found. Please request again.");
    }

    if (!otp.equals(shop.getOtp())) {
        throw new CustomException("Invalid OTP");
    }

    // ✅ Use UTC clock so OTP expiry is consistent across servers/timezones
    if (shop.getOtpExpiry() == null ||
        shop.getOtpExpiry().isBefore(LocalDateTime.now(clock))) {

        throw new CustomException("OTP expired");
    }

    // ✅ Success → verify user
    shop.setVerified(true);
    shop.setOtp(null);
    shop.setOtpExpiry(null);

    shopRepository.save(shop);

    String token = jwtUtil.generateToken(shop.getEmail());

    return new VerifyOtpResponse(
            true,
            "OTP verified successfully",
            shop.getEmail(),
            getRoleNames(shop)
    );
}    // LOGIN
    // ===============================
    public AuthResponse login(LoginRequest request) {

        if (request == null) {
            throw new CustomException("Login request cannot be null");
        }
        String email = normalizeEmail(request.getEmail(), "Email is required");
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new CustomException("Password is required");
        }

        Shop shop = shopRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), shop.getPassword()))
            throw new CustomException("Invalid credentials");

        if (!Boolean.TRUE.equals(shop.getVerified()))
            throw new CustomException("Please verify your account first");

        String token = jwtUtil.generateToken(shop.getEmail());

        return new AuthResponse(
                token,
                shop.getId(),
                shop.getEmail(),
                getRoleNames(shop)
        );
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
        if (request == null) {
            throw new CustomException("Profile update request cannot be null");
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new CustomException("User not found"));

        if (request.getMobileNumber() != null) {
            shop.setMobileNumber(request.getMobileNumber().trim());
        }
        if (request.getDashboardPin() != null) {
            shop.setDashboardPin(request.getDashboardPin().trim());
        }
        if (request.getShopName() != null) {
            shop.setShopName(request.getShopName().trim());
        }
        if (request.getShopOwnerName() != null) {
            shop.setShopOwnerName(request.getShopOwnerName().trim());
        }
        if (request.getAddress() != null) {
            shop.setAddress(request.getAddress().trim());
        }
        if (request.getPincode() != null) {
            shop.setPincode(request.getPincode().trim());
        }

        Shop saved = shopRepository.save(shop);

        return mapToProfile(saved);
    }

    // ===============================
    // UTIL METHODS
    // ===============================

    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private void validateRegisterRequest(RegisterRequest request) {

    if (request == null) {
        throw new CustomException("Register request cannot be null");
    }
    String email = normalizeEmail(request.getEmail(), "Email is required");
    if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
        throw new CustomException("Password is required");
    }
    if (request.getShopName() == null || request.getShopName().trim().isEmpty()) {
        throw new CustomException("Shop name is required");
    }

    Optional<Shop> existingByEmail = shopRepository.findByEmail(email);
    if (existingByEmail.isPresent() &&
            Boolean.TRUE.equals(existingByEmail.get().getVerified())) {
        throw new CustomException("Email already registered");
    }

    if (request.getPassword().length() < 6)
        throw new CustomException("Password must be at least 6 characters");
}

    private String normalizeEmail(String email, String errorMessage) {
    if (email == null || email.trim().isEmpty()) {
        throw new CustomException(errorMessage);
    }
    return email.trim().toLowerCase();
   }

   private void applyRegisterDetails(Shop shop, RegisterRequest request) {
    if (request.getShopName() != null) {
        shop.setShopName(request.getShopName().trim());
    }
    if (request.getAddress() != null) {
        shop.setAddress(request.getAddress().trim());
    }
    if (request.getMobileNumber() != null) {
        shop.setMobileNumber(request.getMobileNumber().trim());
    }
    if (request.getDashboardPin() != null) {
        shop.setDashboardPin(request.getDashboardPin().trim());
    }
    if (request.getShopOwnerName() != null) {
        shop.setShopOwnerName(request.getShopOwnerName().trim());
    }
    if (request.getPincode() != null) {
        shop.setPincode(request.getPincode().trim());
    }
    if (request.getPassword() != null) {
        shop.setPassword(passwordEncoder.encode(request.getPassword().trim()));
    }
   }

   private Set<String> getRoleNames(Shop shop) {
    return shop.getRoles()
            .stream()
            .map(role -> role.getName().name())  // ✅ convert enum to String
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

