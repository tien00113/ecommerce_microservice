package com.micro.common.models;

import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    
    private String orderId;

    private List<OrderItemEvent> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderStatus stockStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus paymentStatus;


    private long price;


    public enum OrderStatus {
        NEW,
        SUCCESS,
        FAILED,
        ROLLBACK
    }
}
