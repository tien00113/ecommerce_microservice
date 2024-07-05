package com.micro.product_service.service.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;

@Service
public class ProductProducer {
    private static final String TOPIC = "product_topic";

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendProductEvent(String key, OrderEvent event) {
        kafkaTemplate.send(TOPIC, key, event);
    }

}
