package com.micro.product_service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderItemUpdate {
    private Long productId;
    private int quantity;
}