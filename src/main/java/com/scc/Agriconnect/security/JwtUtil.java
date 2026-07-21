package com.scc.Agriconnect.security;

import com.scc.Agriconnect.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        var builder = Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getUserId().toString())
                .claim("fullName", user.getFullName())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs));
        if(user.getCooperative() != null){
            builder.claim("cooperativeId", user.getCooperative().getCooperativeId().toString());
            builder.claim("cooperativeStatus", user.getCooperative().getStatus().name());
        }
        return builder.signWith(key()).compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isExpired(token);
    }

    public boolean isExpired(String token){
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token).
                getPayload();
    }
}