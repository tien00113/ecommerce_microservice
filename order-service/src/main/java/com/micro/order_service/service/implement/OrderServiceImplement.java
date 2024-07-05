package com.micro.order_service.service.implement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.models.OrderItemEvent;
import com.micro.order_service.exception.OrderException;
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
import com.micro.order_service.service.client.PaymentClient;
import com.micro.order_service.service.client.ProductClient;
import com.micro.order_service.service.kafka.producer.OrderProducer;

@Service
public class OrderServiceImplement implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private OrderProducer orderProducer;

    @Override
    @Transactional
    public Order createOrder(OrderRequest orderRequest, Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

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
        createOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        createOrder.setPaymentMethod(orderRequest.getPaymentMethod());
        createOrder.setStatus(Order.OrderStatus.NEW);
        createOrder.setStockStatus(Order.OrderStatus.NEW);
        createOrder.setPaymentStatus(Order.OrderStatus.NEW);
        createOrder.setCreateAt(LocalDateTime.now());
        createOrder.setUpdateStatusAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(createOrder);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);

            orderItemRepository.save(orderItem);
        }

        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cart.setTotalItem(0);
        cartRepository.save(cart);

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
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order not found"));
    }

    @Override
    public List<OrderItemEvent> getItemsByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        List<OrderItemEvent> orderItemEvents = order.getOrderItems().stream()
                .map(item -> new OrderItemEvent(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());
        return orderItemEvents; 
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
    
}
