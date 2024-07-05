package com.micro.user_service.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.user_service.config.JwtProvider;
import com.micro.user_service.models.User;
import com.micro.user_service.repository.UserRepository;
import com.micro.user_service.request.LoginRequest;
import com.micro.user_service.request.SignupRequest;
import com.micro.user_service.response.AuthResponse;
import com.micro.user_service.service.CustomerUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerUserService customerUserService;

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody SignupRequest signupRequest) throws Exception{
       
        User isEmailExist = userRepository.findByEmail(signupRequest.getEmail());
        
        if (isEmailExist != null) {
            throw new Exception("Email is already user with another account");
        }

        User newUser = new User();

        newUser.setEmail(signupRequest.getEmail());
        newUser.setUsername(signupRequest.getUsername());
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setRoles(Collections.singleton(User.Role.valueOf(signupRequest.getRole())));
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        User savedUser = userRepository.save(newUser);


        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword(), savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication, savedUser.getId());

        AuthResponse response = new AuthResponse();

        response.setJwt(token);
        response.setMessage("Register successfully");
        response.setStatus(true);

        return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest request){
        Authentication authentication = customerUserService.authenticate(request.getEmail(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication, customerUserService.getUserId(request.getEmail()));

        AuthResponse response = new AuthResponse();

        response.setMessage("Login successfully");
        response.setStatus(true);
        response.setJwt(token);

        log.info("LOGIN_LOG SUCCESSFULLY__________________________________");

        return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
    }
}
