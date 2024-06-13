package com.micro.order_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.micro.order_service.dto.ProductDTO;

@FeignClient(name = "productClient", url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/public/product/{productId}")
    ProductDTO getProduct(@PathVariable("productId") Long productId);

}
