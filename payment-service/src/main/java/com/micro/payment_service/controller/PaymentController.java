package com.micro.payment_service.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.micro.common.models.PaymentRequest;
import com.micro.payment_service.config.VnPayRefund;
import com.micro.payment_service.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/public/payment/submit")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PaymentRequest> submitOrder(@RequestBody PaymentRequest request)
            throws UnsupportedEncodingException {
        // String vnpayUrl = vnPayService.processPaymentMethod(orderTotal, orderId);
        PaymentRequest response  = paymentService.payment(request);

        return new ResponseEntity<PaymentRequest>(response, HttpStatus.OK);
    }

    @GetMapping("/public/payment/vnpay-payment")
    public Map<String, String> getPaymentStatus(HttpServletRequest request) {
        
        Map<String, String> response = paymentService.paymentReturn(request);

        return response;
    }

    @GetMapping("/public/payment/refund")
    public String test(){
        String test = VnPayRefund.testrefund();

        return test;
    }
}
