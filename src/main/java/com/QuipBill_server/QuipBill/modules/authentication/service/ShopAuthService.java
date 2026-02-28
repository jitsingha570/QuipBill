package com.QuipBill_server.QuipBill.modules.authentication.service;
//===== importing classes from Spring framework =====
import org.springframework.stereotype.Service;// Indicates that this class is a service component in the Spring framework, which means it contains business logic and can be injected into other components such as controllers.
import org.springframework.transaction.annotation.Transactional;// Indicates that the methods in this class should be executed within a transactional context, which means that if any method throws an exception, the transaction will be rolled back to maintain data integrity.
import org.springframework.security.crypto.password.PasswordEncoder;// Used for encoding passwords before storing them in the database and for verifying passwords during login.

//===== importing classes from our project =====
import com.QuipBill_server.QuipBill.modules.authentication.dto.RegisterRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.LoginRequest;
import com.QuipBill_server.QuipBill.modules.authentication.dto.AuthResponse;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import com.QuipBill_server.QuipBill.modules.authentication.repository.RoleRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Role;
import com.QuipBill_server.QuipBill.modules.authentication.entity.RoleName;
import com.QuipBill_server.QuipBill.modules.authentication.security.JwtUtil;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;
import com.QuipBill_server.QuipBill.modules.authentication.service.EmailService;
//===== importing other necessary classes =====
import java.util.Set; // Used for handling collections of roles assigned to a shop
import java.util.Optional;
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
   public String register(RegisterRequest request) {

    validateRegisterRequest(request);

    String email = request.getEmail().trim().toLowerCase();

    Optional<Shop> existingShopOpt = shopRepository.findByEmail(email);

    // 🔎 CHECK IF EMAIL ALREADY EXISTS
    if (existingShopOpt.isPresent()) {

        Shop existingShop = existingShopOpt.get();

        // ❌ If already verified → block registration
        if (Boolean.TRUE.equals(existingShop.getVerified())) {
            throw new CustomException("Email already registered");
        }

        // 🔁 If NOT verified → regenerate OTP and resend
        String otp = generateOtp();
        existingShop.setOtp(otp);
        existingShop.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        shopRepository.save(existingShop);
        emailService.sendOtpEmail(existingShop.getEmail(), otp);

        return "OTP resent to your email";
    }

    // 🆕 NEW USER REGISTRATION
    Shop shop = new Shop();
    shop.setUsername(request.getUsername().trim());
    shop.setEmail(email);
    shop.setShopName(request.getShopName());
    shop.setAddress(request.getAddress());
    shop.setLatitude(request.getLatitude());
    shop.setLongitude(request.getLongitude());
    shop.setPassword(passwordEncoder.encode(request.getPassword()));
    shop.setVerified(false);

    // ✅ Assign role
    Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
            .orElseThrow(() -> new CustomException("Role not found"));

    shop.addRole(role);

    // 🔐 Generate OTP
    String otp = generateOtp();
    shop.setOtp(otp);
    shop.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

    shopRepository.save(shop);

    emailService.sendOtpEmail(shop.getEmail(), otp);

    return "OTP sent to your email";
}

  // ===============================
    // VERIFY OTP
    // ===============================
  public AuthResponse verifyOtp(String username, String otp) {

    Shop shop = shopRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException("User not found"));

    if (Boolean.TRUE.equals(shop.getVerified()))
        throw new CustomException("Account already verified");

    if (shop.getOtp() == null)
        throw new CustomException("OTP not found. Please request again.");

    if (!otp.equals(shop.getOtp()))
        throw new CustomException("Invalid OTP");

    if (shop.getOtpExpiry() == null ||
        shop.getOtpExpiry().isBefore(LocalDateTime.now()))
        throw new CustomException("OTP expired");

    shop.setVerified(true);
    shop.setOtp(null);
    shop.setOtpExpiry(null);

    shopRepository.save(shop);

    String token = jwtUtil.generateToken(shop.getUsername());

    return new AuthResponse(
            token,
            shop.getUsername(),
            getRoleNames(shop)
    );
}    // ===============================
    // LOGIN
    // ===============================
    public AuthResponse login(LoginRequest request) {

        Shop shop = shopRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), shop.getPassword()))
            throw new CustomException("Invalid credentials");

        if (!Boolean.TRUE.equals(shop.getVerified()))
            throw new CustomException("Please verify your account first");

        String token = jwtUtil.generateToken(shop.getUsername());

        return new AuthResponse(
                token,
                shop.getUsername(),
                getRoleNames(shop)
        );
    }

    // ===============================
    // UTIL METHODS
    // ===============================

    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private void validateRegisterRequest(RegisterRequest request) {

    String username = request.getUsername().trim();
    String email = request.getEmail().trim().toLowerCase();

    Optional<Shop> existingByUsername = shopRepository.findByUsername(username);
    if (existingByUsername.isPresent() &&
            Boolean.TRUE.equals(existingByUsername.get().getVerified())) {
        throw new CustomException("Username already exists");
    }

    Optional<Shop> existingByEmail = shopRepository.findByEmail(email);
    if (existingByEmail.isPresent() &&
            Boolean.TRUE.equals(existingByEmail.get().getVerified())) {
        throw new CustomException("Email already registered");
    }

    if (request.getPassword().length() < 6)
        throw new CustomException("Password must be at least 6 characters");
}


   private Set<String> getRoleNames(Shop shop) {
    return shop.getRoles()
            .stream()
            .map(role -> role.getName().name())  // ✅ convert enum to String
            .collect(Collectors.toSet());
}
}