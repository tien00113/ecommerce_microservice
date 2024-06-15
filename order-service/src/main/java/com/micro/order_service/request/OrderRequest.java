package com.micro.order_service.request;

import lombok.Getter;

@Getter
public class OrderRequest {
    // private Cart cart;
    private String note;
    private String address;
    private String phoneNumber;
}
