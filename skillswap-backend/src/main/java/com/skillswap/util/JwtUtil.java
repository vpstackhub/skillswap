package com.skillswap.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION_MS = 3_600_000L; // 1 hour

    private final SecretKey SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String base64Secret) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Secret.trim());
            if (keyBytes.length < 32) {
                throw new IllegalStateException(
                    "jwt.secret must be base64 for at least 32 bytes (256-bit) for HS256."
                );
            }
            this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(
                "Invalid base64 jwt.secret. Ensure application-local.properties has a clean base64 string only.",
                ex
            );
        }
    }

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        Object r = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
        return r != null ? r.toString() : null;
    }

    public void validateTokenOrThrow(String token) {
        // Will throw if invalid/expired
        Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
    }

    // Back-compat for existing callers (returns the email/subject)
    public String validateToken(String token) {
        return getEmailFromToken(token);
    }
}