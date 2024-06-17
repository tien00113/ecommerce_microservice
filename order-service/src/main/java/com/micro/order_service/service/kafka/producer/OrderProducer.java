package com.micro.order_service.service.kafka.producer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.common.models.OrderItemEvent;
import com.micro.order_service.models.Order;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProducer {
    private static final String ORDER_TOPIC = "order_topic";
    private static final String PAYMENT_TOPIC = "payment_topic";

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(Order order){
        OrderEvent orderEvent = convertOrderToOrderEvent(order);
        // String payload = orderEvent.toString();
        log.info(orderEvent.toString());

        kafkaTemplate.send(ORDER_TOPIC, orderEvent.getOrderId(), orderEvent);
    }

    public OrderEvent convertOrderToOrderEvent(Order order) {
        List<OrderItemEvent> orderItemEvents = order.getOrderItems().stream()
                .map(item -> new OrderItemEvent(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        return new OrderEvent(
                order.getOrderId(),
                orderItemEvents,
                convertOrderStatus(order.getStatus()),
                convertOrderStatus(order.getStockStatus()),
                convertOrderStatus(order.getPaymentStatus()),
                order.getTotalPrice()
        );
    }

    public static OrderStatus convertOrderStatus(Order.OrderStatus status) {
        return OrderStatus.valueOf(status.name());
    }
}
