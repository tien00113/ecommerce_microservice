package com.micro.order_service.service;

import com.micro.order_service.models.Cart;

public interface CartService {
    public String clearCart(Long userId);

    public String removeCartItem(Long cartItemId, Long userId);

    public Cart getUserCart(Long userId);
}
