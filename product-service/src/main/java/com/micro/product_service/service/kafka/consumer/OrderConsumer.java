package com.micro.product_service.service.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.models.OrderEvent;
import com.micro.product_service.service.OrderManagementService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderConsumer {
  
    @Autowired
    private OrderManagementService orderManagementService;

    @KafkaListener(topics = "order_topic", groupId = "order-service-group")
    @Transactional
    public void updateStockConsume(OrderEvent event) {
        try {
            log.info("Received order event: {}", event);
            orderManagementService.processStock(event);
        } catch (Exception e) {
            log.error("Error processing order event: {}", event, e);
            throw new RuntimeException("Failed to process order event", e); // Adjust error handling as per your application's needs
        }
    }
}
