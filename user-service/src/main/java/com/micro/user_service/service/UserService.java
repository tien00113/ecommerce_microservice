package com.micro.user_service.service;

import com.micro.user_service.models.User;

public interface UserService {
    
    public User getUserProfile(String jwt);
}
