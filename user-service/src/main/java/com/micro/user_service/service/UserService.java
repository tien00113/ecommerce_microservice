package com.micro.user_service.service;

import com.micro.user_service.dto.UserDTO;

public interface UserService {
    
    public UserDTO getUserProfile(String jwt);
}
