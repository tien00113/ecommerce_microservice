package com.micro.order_service.request;

import com.micro.order_service.models.Cart;

import lombok.Getter;

@Getter
public class CartItemRequest {
    private Long productId;
    private Cart cart;
    private String color;
    private String size;
    private int quantity;
}
