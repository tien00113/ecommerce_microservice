package com.micro.payment_service.service;

import java.util.Map;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.PaymentRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentRequest payment(PaymentRequest request);

    boolean rollbackPayment(OrderEvent orderEvent);

    Map<String, String> paymentReturn(HttpServletRequest request);
}
