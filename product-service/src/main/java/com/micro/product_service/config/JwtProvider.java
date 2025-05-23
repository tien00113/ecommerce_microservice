package com.micro.product_service.config;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    static SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECRET_KEY.getBytes());

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public static List<String> getRolesFromJwtToken(String jwt) {
        List<?> rawAuthorities = extractAllClaims(jwt).get("authorities", List.class);

        List<String> authorities = new ArrayList<>();
        for (Object authority : rawAuthorities) {
            if (authority instanceof String) {
                authorities.add((String) authority);
            } else {
                throw new BadCredentialsException("Invalid authority type");
            }
        }

        return authorities;
    }

    public static String getEmailFromJwtToken(String jwt) {
        Claims claim = extractAllClaims(jwt);
        return String.valueOf(claim.get("email"));
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
