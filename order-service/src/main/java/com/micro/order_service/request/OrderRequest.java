package com.micro.order_service.request;

import com.micro.order_service.models.Cart;

import lombok.Getter;

@Getter
public class OrderRequest {
    private Cart cart;
    private String note;
    private String address;
}
