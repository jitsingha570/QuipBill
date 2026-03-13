package com.QuipBill_server.QuipBill.modules.authentication.security;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ShopRepository shopRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, ShopRepository shopRepository) {
        this.jwtUtil = jwtUtil;
        this.shopRepository = shopRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = authHeader.substring(7);

            try {
                String email = jwtUtil.extractUsername(token);
                Shop shop = shopRepository.findByEmail(email).orElse(null);

                if (shop != null && Boolean.TRUE.equals(shop.getVerified()) && Boolean.TRUE.equals(shop.getActive())) {
                    var authorities = shop.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                            .collect(Collectors.toList());

                    // Principal is shop id so existing controller code can parse it as Long.
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(String.valueOf(shop.getId()), null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException | IllegalArgumentException ignored) {
                // Keep context unauthenticated; Spring Security will handle protected routes.
            }
        }

        filterChain.doFilter(request, response);
    }
}
