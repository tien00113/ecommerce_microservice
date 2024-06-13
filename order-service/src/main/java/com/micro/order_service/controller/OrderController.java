package com.micro.order_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    @GetMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public String getMethodName() {
        return "helllo order";
    }
    
}
