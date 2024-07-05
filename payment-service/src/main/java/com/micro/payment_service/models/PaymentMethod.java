package com.micro.payment_service.models;

import com.micro.common.models.PaymentRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentMethod {
    public PaymentRequest processPaymentMethod(long amount, String orderId, Long userId, String email);

    public int orderReturn(HttpServletRequest request);
}
