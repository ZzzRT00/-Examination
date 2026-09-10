package com.xhj.examination.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "examinationSystemSecretKey2026Secure";
    private static final long EXPIRE = 7 * 24 * 60 * 60 * 1000L;

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String createToken(Long userId, String identity) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("role", identity)
                .expiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(getSigningKey())
                .compact();
    }

    public static Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
