package com.micro.payment_service.service.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;

@Service
public class PaymentProducer {
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String PAYMENT_TOPIC = "payment_topic";

    public void sendPaymentTopic(String key, OrderEvent orderEvent){
        
        kafkaTemplate.send(PAYMENT_TOPIC, key, orderEvent);
    }
}
