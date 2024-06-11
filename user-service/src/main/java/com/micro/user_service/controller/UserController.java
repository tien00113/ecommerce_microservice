package com.micro.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.micro.user_service.models.User;
import com.micro.user_service.service.UserService;

@RestController
// @RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){

        return new ResponseEntity<User>(userService.getUserProfile(jwt), HttpStatus.OK);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<String> getUserProfile2(@RequestHeader("Authorization") String jwt){

        return new ResponseEntity<>("okeokeoekoekoe", HttpStatus.OK);
    }
}
