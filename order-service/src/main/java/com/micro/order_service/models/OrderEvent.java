package com.micro.order_service.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class OrderEvent {
    private List<OrderItemUpdate> updates;

    public OrderEvent(List<OrderItemUpdate> updates) {
        this.updates = updates;
    }

    @AllArgsConstructor
    @Getter
    public static class OrderItemUpdate {
        private Long productVariantId;
        private int quantity;
    }
}
