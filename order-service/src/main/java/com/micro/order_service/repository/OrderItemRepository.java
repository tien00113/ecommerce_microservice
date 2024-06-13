package com.micro.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.order_service.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
