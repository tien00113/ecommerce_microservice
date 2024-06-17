package com.micro.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.order_service.models.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
    public Order findByOrderId(String orderId);
}
