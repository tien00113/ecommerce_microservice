package com.micro.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.order_service.models.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    CartItem findByProductId(Long productId);
    CartItem findByUserIdAndProductId(Long userId, Long productId);
}
