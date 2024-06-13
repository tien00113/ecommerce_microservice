package com.micro.order_service.service;

import com.micro.order_service.models.CartItem;
import com.micro.order_service.request.CartItemRequest;

public interface CartItemService {
    public CartItem addToCart(Long userId, CartItemRequest cartItemRequest) throws Exception;

    public CartItem increaseQuantity(Long cartItemId) throws Exception;

    public CartItem decreaseQuantity(Long cartItemId) throws Exception;
}
