package com.QuipBill_server.QuipBill.modules.authentication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${jwt.access.secret}") String accessSecret,
            @Value("${jwt.refresh.secret}") String refreshSecret,
            @Value("${jwt.access.expiration-ms}") long accessExpirationMs,
            @Value("${jwt.refresh.expiration-ms}") long refreshExpirationMs
    ) {
        if (accessSecret == null || accessSecret.isBlank()) {
            throw new IllegalStateException(
                    "Missing JWT secret. Set env var JWT_SECRET (or JWT_ACCESS_SECRET/ACCESS_SECRET) or property jwt.access.secret."
            );
        }
        if (refreshSecret == null || refreshSecret.isBlank()) {
            throw new IllegalStateException(
                    "Missing JWT secret. Set env var JWT_SECRET (or JWT_REFRESH_SECRET/REFRESH_SECRET) or property jwt.refresh.secret."
            );
        }

        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));

        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token, boolean isRefresh) {
        return parseClaims(token, isRefresh).getSubject();
    }

    public boolean validateToken(String token, boolean isRefresh) {
        try {
            parseClaims(token, isRefresh);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token, boolean isRefresh) {
        SecretKey key = isRefresh ? refreshKey : accessKey;
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }
}
