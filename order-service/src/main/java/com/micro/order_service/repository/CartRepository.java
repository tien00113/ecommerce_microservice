package com.micro.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.order_service.models.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    public Cart findByUserId(Long userId);
}
