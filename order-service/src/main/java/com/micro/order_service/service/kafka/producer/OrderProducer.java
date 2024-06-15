package com.micro.order_service.service.kafka.producer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.micro.order_service.models.Order;
import com.micro.order_service.models.OrderEvent;
import com.micro.order_service.models.OrderItem;

@Service
public class OrderProducer {
    private static final String TOPIC = "order_topics";

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        Map<Long, Integer> productQuantities = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            Long productId = orderItem.getProductId();
            int quantity = orderItem.getQuantity();

            if (productQuantities.containsKey(productId)) {
                productQuantities.put(productId, productQuantities.get(productId) + quantity);
            } else {
                productQuantities.put(productId, quantity);
            }
        }

        kafkaTemplate.send(TOPIC, createAggregateMessage(productQuantities));
    }

    private OrderEvent createAggregateMessage(Map<Long, Integer> productQuantities) {
        List<OrderEvent.OrderItemUpdate> updates = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            updates.add(new OrderEvent.OrderItemUpdate(entry.getKey(), entry.getValue()));
        }

        return new OrderEvent(updates);
    }
}
