package com.micro.user_service.converter;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority{
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
    
}
