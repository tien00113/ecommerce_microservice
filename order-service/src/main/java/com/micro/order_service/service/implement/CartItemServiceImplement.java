package com.micro.order_service.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.micro.order_service.config.Urls;
import com.micro.order_service.dto.ProductDTO;
import com.micro.order_service.models.Cart;
import com.micro.order_service.models.CartItem;
import com.micro.order_service.repository.CartItemRepository;
import com.micro.order_service.repository.CartRepository;
import com.micro.order_service.request.CartItemRequest;
import com.micro.order_service.service.CartItemService;
import com.micro.order_service.service.client.ProductClient;

@Service
public class CartItemServiceImplement implements CartItemService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public CartItem addToCart(Long userId, CartItemRequest cartItemRequest) throws Exception {

        ProductDTO product = productClient.getProduct(cartItemRequest.getProductId());

        if (product != null) {
            System.out.println("---------------------------" + product);

            Cart existCart = cartRepository.findByUserId(userId);

            if (existCart == null) {
                existCart = new Cart();

                existCart.setUserId(userId);
                existCart = cartRepository.save(existCart);
            }

            CartItem existingItem = cartItemRepository.findByCartAndProductIdAndColorAndSizeAndUserId(existCart,
                    product.getId(), cartItemRequest.getColor(), cartItemRequest.getSize(), userId);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
                existingItem.setPrice(product.getPrice() * existingItem.getQuantity());

                existCart.getCartItems().add(existingItem);
                existCart.setTotalPrice(
                        existCart.getTotalPrice() + (product.getPrice() * cartItemRequest.getQuantity()));

                return cartItemRepository.save(existingItem);

            } else {

                CartItem newItem = new CartItem();

                newItem.setProductId(product.getId());
                newItem.setCart(existCart);
                newItem.setQuantity(cartItemRequest.getQuantity());
                newItem.setColor(cartItemRequest.getColor());
                newItem.setSize(cartItemRequest.getSize());
                newItem.setPrice(product.getPrice() * newItem.getQuantity());
                newItem.setUserId(userId);

                existCart.getCartItems().add(newItem);
                existCart.setTotalPrice(existCart.getTotalPrice() + newItem.getPrice());

                return cartItemRepository.save(newItem);
            }
        } else {
            throw new Exception("get product failed -----------------------");
        }
    }

    @Override
    public CartItem increaseQuantity(Long cartItemId) throws Exception {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("CartItem not found with id: " + cartItemId));

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setPrice((cartItem.getPrice() / (cartItem.getQuantity() - 1)) * cartItem.getQuantity());
        }

        return cartItemRepository.save(cartItem);

    }

    @Override
    public CartItem decreaseQuantity(Long cartItemId) throws Exception {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("CartItem not found with id: " + cartItemId));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setPrice((cartItem.getPrice() / (cartItem.getQuantity() + 1)) * cartItem.getQuantity());

            return cartItemRepository.save(cartItem);

        } else {
            cartItemRepository.delete(cartItem);

            return null;
        } 
    }

}