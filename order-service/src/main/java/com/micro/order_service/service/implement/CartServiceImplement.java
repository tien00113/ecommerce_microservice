package com.micro.order_service.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.order_service.models.Cart;
import com.micro.order_service.models.CartItem;
import com.micro.order_service.repository.CartItemRepository;
import com.micro.order_service.repository.CartRepository;
import com.micro.order_service.service.CartService;

@Service
public class CartServiceImplement implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public String clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            cart.getCartItems().clear();

            cart.setTotalItem(0);
            cart.setTotalPrice(0);

            cartRepository.save(cart);

            return "Clear cart completed";
        } else {
            return "Clear cart err";
        }

    }

    @Override
    public String removeCartItem(Long cartItemId, Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartItems();

            CartItem itemToRemove = cartItems.stream()
                    .filter(cartItem -> cartItemId.equals(cartItem.getId()))
                    .findFirst()
                    .orElse(null);

            if (itemToRemove != null) {
                cart.setTotalPrice(cart.getTotalPrice() - itemToRemove.getPrice());
                cartItems.remove(itemToRemove);

                cartItemRepository.delete(itemToRemove);
                cartRepository.save(cart);
                return "remove cart item successfully";
            } else {
                return "not found cart item id: " + cartItemId;
            }
        } else {
            return "cart user null: " + userId;
        }
    }

    @Override
    public Cart getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    
}
