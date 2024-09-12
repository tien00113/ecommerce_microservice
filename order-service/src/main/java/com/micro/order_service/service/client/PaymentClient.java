package com.micro.order_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.micro.common.models.PaymentRequest;

@FeignClient(name = "paymentClient", url = "${url.payment}")
public interface PaymentClient {
    @PostMapping("/public/payment/submit")
    PaymentRequest getUrlPayment(@RequestBody PaymentRequest request);
}
