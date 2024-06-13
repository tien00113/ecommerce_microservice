package com.micro.api_gateway.utils;

import java.security.Key;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    public static final String SECRET_KEY = "uU6P1xsPvF7yO6oL5uZd5M3AotBfUknC7D3OZvjLw5mcxsP3rzgR3X4n1yG7d8P4Nl9W7yR8E1k5P3C6X8Q1E9M2J3J7N1F9";

    public void validateToken(final String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
            System.out.println("Claims: " + claims.get("userId"));
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            // throw e;
            throw new RuntimeException("Token validation failed: " + e.getMessage(), e);
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        try {
            System.out.println("Extracting claims for token: " + token);
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println("Error extracting claims: " + e.getMessage());
            throw new RuntimeException("Error extracting claims: " + e.getMessage(), e);
        }
    }
}
