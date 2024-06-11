package com.micro.user_service.models;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;

import com.micro.user_service.converter.CustomGrantedAuthority;
import com.micro.user_service.converter.RoleListConverter;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String firstName;
    private String lastName;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private Set<Address> address = new HashSet<>();
    
    private String phoneNumber;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles")
    @Convert(converter = RoleListConverter.class)
    private Collection<Role> roles;

    @Override
    public Collection<? extends CustomGrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new CustomGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public enum Role {
        USER,
        ADMIN
    }
}
