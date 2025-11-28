package com.capstone.airlineticketreservationsystem.utilities;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // Default 24 hours
    private Long expiration;

    @Value("${jwt.verification.expiration}")
    private Long verificationExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateVerificationToken(String userUUID, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + verificationExpiration);

        return Jwts.builder()
                .setSubject(userUUID)
                .claim("email", email)
                .claim("purpose", "password_setup")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateTokenAndGetUserUUID(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Verify this is a password setup token
            if (!"password_setup".equals(claims.get("purpose"))) {
                throw new JwtException("Invalid token purpose");
            }

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token has expired");
        } catch (JwtException e) {
            throw new JwtException("Invalid token");
        }
    }

    /*
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
    }
     */

    public String generateAccessToken(String userUUID, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userUUID) // UUID as subject (primary identifier)
                .claim("email", email) // Email as custom claim
                .claim("role", role) // Role as custom claim
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserUUIDFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject(); // Subject is now the userUUID
    }

    public String getEmailFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}