package com.micro.order_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.micro.order_service.dto.ProductDTO;
import com.micro.order_service.dto.ProductVariantDTO;

@FeignClient(name = "productClient", url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/public/product/{productId}")
    ProductDTO getProduct(@PathVariable("productId") Long productId);

    @PatchMapping("public/product/variants/{variantId}/quantity")
    void updateQuantity(@PathVariable Long variantId,
            @RequestParam Long quantity);

    @GetMapping("/public/product/variant/{id}")
    ProductVariantDTO getProductVariant(@PathVariable Long id);

}
