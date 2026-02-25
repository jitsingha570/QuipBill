package com.QuipBill_server.QuipBill.modules.authentication.service;

import com.QuipBill_server.QuipBill.modules.authentication.dto.*;
import com.QuipBill_server.QuipBill.modules.authentication.entity.*;
import com.QuipBill_server.QuipBill.modules.authentication.repository.*;
import com.QuipBill_server.QuipBill.modules.authentication.security.JwtUtil;
import com.QuipBill_server.QuipBill.modules.authentication.exception.CustomException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ShopAuthService {

    private final ShopRepository shopRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ShopAuthService(ShopRepository shopRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.shopRepository = shopRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {

        if (shopRepository.existsByUsername(request.getUsername()))
            throw new CustomException("Username already exists");

        if (shopRepository.existsByEmail(request.getEmail()))
            throw new CustomException("Email already exists");

        Shop shop = new Shop();
        shop.setUsername(request.getUsername());
        shop.setEmail(request.getEmail());
        shop.setShopName(request.getShopName());
        shop.setAddress(request.getAddress());
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());
        shop.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("SHOP")
                .orElseThrow(() -> new CustomException("Role not found"));

        shop.addRole(role);
        shopRepository.save(shop);

        String token = jwtUtil.generateToken(shop.getUsername());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        Shop shop = shopRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("Invalid username"));

        if (!passwordEncoder.matches(request.getPassword(), shop.getPassword()))
            throw new CustomException("Invalid password");

        String token = jwtUtil.generateToken(shop.getUsername());

        return new AuthResponse(token);
    }
}