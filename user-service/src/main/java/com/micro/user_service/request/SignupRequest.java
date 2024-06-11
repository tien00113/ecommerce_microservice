package com.micro.user_service.request;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
}
