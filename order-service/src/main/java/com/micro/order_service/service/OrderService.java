package com.micro.order_service.service;

import com.micro.order_service.models.Order;
import com.micro.order_service.models.Order.OrderStatus;
import com.micro.order_service.request.OrderRequest;

public interface OrderService {
    public Order createOrder(OrderRequest orderRequest, Long userId);

    public String deleteOrder(Long orderId);

    public Order updateOrderStatus(Long orderId, OrderStatus status);
    
    public Order getOrderById(Long orderId);
}
