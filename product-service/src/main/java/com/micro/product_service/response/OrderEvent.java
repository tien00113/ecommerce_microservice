package com.micro.product_service.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderEvent {

    private List<OrderItemUpdate> updates;
}
