package com.micro.product_service.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.micro.common.models.OrderItemEvent;

@FeignClient(name = "orderClient", url = "http://localhost:8084")
public interface OrderClient {
    @GetMapping("/public/order/items/{orderId}")
    List<OrderItemEvent> getItemEvents(@PathVariable String orderId);

}
