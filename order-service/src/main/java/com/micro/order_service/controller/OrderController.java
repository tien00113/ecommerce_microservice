package com.micro.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.micro.common.models.OrderEvent;
import com.micro.order_service.config.JwtProvider;
import com.micro.order_service.models.Order;
import com.micro.order_service.request.OrderRequest;
import com.micro.order_service.service.OrderService;


@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @GetMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public String getMethodName() {
        return "helllo order";
    }

    @PostMapping("/private/order")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrderRequest orderRequest){
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);
        Order order = orderService.createOrder(orderRequest, userId);

        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    // @PostMapping("/public/order/test")
    // public String postMethodName(@RequestBody String entity) {
        

    //     kafkaTemplate.send("order_topic", entity);

    //     System.out.println("9999999999999999999999999999999999999999999999999" + entity);
    //     return entity;
    // }
    
    
}
