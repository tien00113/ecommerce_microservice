package com.micro.product_service.models;

import lombok.Getter;

@Getter
public class OrderEvent {
    private int quantity;
    private Long productVariantId;
}
