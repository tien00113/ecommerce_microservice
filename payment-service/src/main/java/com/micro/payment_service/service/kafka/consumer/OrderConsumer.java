package com.micro.payment_service.service.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.models.OrderEvent;
import com.micro.payment_service.service.OrderServiceManagement;

@Service
public class OrderConsumer {

    @Autowired
    private OrderServiceManagement orderServiceManagement;
    
    // @KafkaListener(topics = "order_topic", groupId = "payment-group")
    // @Transactional
    // public void paymentListener(OrderEvent orderEvent){
    //     try {
    //     orderServiceManagement.processPaymentOrder(orderEvent);
    //     } catch (Exception e) {
    //         throw new RuntimeException("Lỗi payment----------------------------------------------", e);
    //     }
    // }
}
