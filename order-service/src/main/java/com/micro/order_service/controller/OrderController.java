package com.micro.order_service.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderItemEvent;
import com.micro.common.models.PaymentRequest;
import com.micro.order_service.config.JwtProvider;
import com.micro.order_service.models.Order;
import com.micro.order_service.request.OrderRequest;
import com.micro.order_service.service.OrderService;
import com.micro.order_service.service.client.PaymentClient;


@RestController
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    private PaymentClient paymentClient;

    @GetMapping("/order")
    @PreAuthorize("hasRole('ADMIN')")
    public String getMethodName() {
        return "helllo order";
    }

    @PostMapping("/private/order")
    public ResponseEntity<PaymentRequest> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrderRequest orderRequest){
        Long userId = JwtProvider.getUserIdFromJwtToken(jwt);
        String subjwt = jwt.substring(7);
        String email = JwtProvider.getEmailFromJwtToken(subjwt);
        Order order = orderService.createOrder(orderRequest, userId);

        PaymentRequest paymentRequest = new PaymentRequest();

        paymentRequest.setEmail(email);
        paymentRequest.setOrderId(order.getOrderId());
        paymentRequest.setMethod(order.getPaymentMethod());
        paymentRequest.setUserId(userId);
        paymentRequest.setPrice(order.getTotalPrice());

        PaymentRequest response = paymentClient.getUrlPayment(paymentRequest);

        return new ResponseEntity<PaymentRequest>(response, HttpStatus.CREATED);
    }

    @GetMapping("/public/order/items/{orderId}")
    public ResponseEntity<List<OrderItemEvent>> getOrderItemEvent(@PathVariable String orderId){
        List<OrderItemEvent> items = orderService.getItemsByOrderId(orderId);

        return new ResponseEntity<List<OrderItemEvent>>(items, HttpStatus.OK);
    }

    @GetMapping("/public/order/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId){
        return new ResponseEntity<Order>(orderService.getOrderByOrderId(orderId), HttpStatus.OK);
    }
    
}
