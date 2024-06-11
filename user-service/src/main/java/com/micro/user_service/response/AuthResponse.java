package com.micro.user_service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

    private String jwt;
    private String message;
    private Boolean status;

}
