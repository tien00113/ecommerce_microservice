package com.micro.order_service.service;

public interface CartService {
    public String clearCart(Long userId);

    public String removeCartItem(Long cartItemId, Long userId);
}
