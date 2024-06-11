package com.micro.api_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.micro.api_gateway.utils.JwtUtil;

@Component
public class AuthenFilter extends AbstractGatewayFilterFactory<AuthenFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
                }

                
                try {
                    System.out.println("Token is valid: " + authHeader);
                    jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    System.out.println("invalid access...! " + authHeader);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access: " + e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
