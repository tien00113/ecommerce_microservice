package com.micro.order_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.micro.order_service.dto.ProductDTO;
import com.micro.order_service.dto.ProductVariantDTO;

@FeignClient(name = "productClient", url = "${url.product}")
public interface ProductClient {

    @GetMapping("/public/product/{productId}")
    ProductDTO getProduct(@PathVariable("productId") Long productId);

    //Get product variantion information
    @GetMapping("/public/product/variant/{productVariantId}")
    ProductVariantDTO getProductVariant(@PathVariable Long productVariantId);

}
