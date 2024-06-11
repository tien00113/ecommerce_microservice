package com.micro.user_service.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConst.JWT_HEADER);

        if (jwt != null) {
            jwt = jwt.substring(7);

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConst.SECRET_KEY.getBytes());
                Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claim.get("email"));
                // String authorities = String.valueOf(claim.get("authorities"));
                List<?> rawAuthorities = claim.get("authorities", List.class);

                List<String> authorities = new ArrayList<>();
                for (Object authority : rawAuthorities) {
                    if (authority instanceof String) {
                        authorities.add((String) authority);
                    } else {
                        throw new BadCredentialsException("Invalid authority type");
                    }
                }

                System.out.println("-----------------------------------------------------------------" + authorities);

                List<GrantedAuthority> auths = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // List<GrantedAuthority> auths =
                // AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
                        + auths.get(0).getAuthority());
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new BadCredentialsException("Token không hợp lệ user jwt validator");
            }
        }

        filterChain.doFilter(request, response);
    }
}
