package com.QuipBill_server.QuipBill.modules.authentication.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final Dotenv dotenv = Dotenv.load();

    // 🔐 Load from .env
    private final String ACCESS_SECRET = dotenv.get("ACCESS_SECRET");
    private final String REFRESH_SECRET = dotenv.get("REFRESH_SECRET");

    private final long ACCESS_EXPIRATION = Long.parseLong(dotenv.get("ACCESS_EXPIRATION"));
    private final long REFRESH_EXPIRATION = Long.parseLong(dotenv.get("REFRESH_EXPIRATION"));

    private final SecretKey accessKey =
            Keys.hmacShaKeyFor(ACCESS_SECRET.getBytes(StandardCharsets.UTF_8));

    private final SecretKey refreshKey =
            Keys.hmacShaKeyFor(REFRESH_SECRET.getBytes(StandardCharsets.UTF_8));

    // 🔹 Access Token
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
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

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}