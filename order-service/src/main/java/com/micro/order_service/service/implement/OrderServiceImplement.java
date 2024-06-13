package com.micro.order_service.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.order_service.models.Cart;
import com.micro.order_service.models.CartItem;
import com.micro.order_service.models.Order;
import com.micro.order_service.models.Order.OrderStatus;
import com.micro.order_service.models.OrderItem;
import com.micro.order_service.repository.CartRepository;
import com.micro.order_service.repository.OrderItemRepository;
import com.micro.order_service.repository.OrderRepository;
import com.micro.order_service.request.OrderRequest;
import com.micro.order_service.service.OrderService;

@Service
public class OrderServiceImplement implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(OrderRequest orderRequest, Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setColor(cartItem.getColor());
            orderItem.setSize(cartItem.getSize());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setUserId(cartItem.getUserId());
            
            OrderItem createdOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(createdOrderItem);
        }

        Order createOrder = new Order();

        createOrder.setUserId(userId);
        createOrder.setOrderItems(orderItems);
        createOrder.setTotalPrice(cart.getTotalPrice());
        createOrder.setNote(orderRequest.getNote());
        createOrder.setAddress(orderRequest.getAddress());
        createOrder.setStatus(OrderStatus.PLACED);
        createOrder.setCreateAt(LocalDateTime.now());
        createOrder.setUpdateStatusAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createOrder);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);

            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }

    @Override
    public String deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return "order deleted successfully";
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order.setUpdateStatusAt(LocalDateTime.now());
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    
}
