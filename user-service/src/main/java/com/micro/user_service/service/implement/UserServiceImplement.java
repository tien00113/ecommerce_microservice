package com.micro.user_service.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.user_service.config.JwtProvider;
import com.micro.user_service.models.User;
import com.micro.user_service.repository.UserRepository;
import com.micro.user_service.service.UserService;

@Service
public class UserServiceImplement implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public User getUserProfile(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);

        return userRepository.findByEmail(email);
    }
    
}
