package com.micro.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.order_service.models.Cart;
import com.micro.order_service.models.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    CartItem findByCartAndProductIdAndColorAndSizeAndUserId(Cart cart, Long productId, String color, String size, Long userId);
}
