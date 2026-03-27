package com.QuipBill_server.QuipBill.modules.authentication.security;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path == null || path.isEmpty()) {
            path = request.getRequestURI();
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        return "/api/auth/register".equals(path)
                || "/api/auth/verify-otp".equals(path)
                || "/api/auth/login".equals(path)
                || "/api/auth/refresh".equals(path)
                || "/api/health".equals(path)
                || "/api/health/db".equals(path)
                || "/health".equals(path)
                || "/health/db".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // ✅ Check header
        if (authHeader != null && authHeader.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            try {
                // 🔥 Extract shopId (NOT email anymore)
                String shopIdStr = jwtUtil.extractUsername(token, false);

                // 🔥 Validate access token
                if (!jwtUtil.validateToken(token, false)) {
                    throw new JwtException("Invalid token");
                }

                Long shopId = Long.parseLong(shopIdStr);

                Shop shop = shopRepository.findById(shopId).orElse(null);

                if (shop != null
                        && Boolean.TRUE.equals(shop.getVerified())
                        && Boolean.TRUE.equals(shop.getActive())) {

                    var authorities = shop.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    String.valueOf(shop.getId()),
                                    null,
                                    authorities
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (ExpiredJwtException e) {
                // 🔥 Token expired → send 403 (frontend will refresh)
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Token expired");
                return;

            } catch (JwtException | IllegalArgumentException e) {
                // 🔥 Invalid token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
