package com.micro.product_service.service.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.micro.product_service.dto.ProductVariantDTO;

@Service
public class ProductProducer {
    private static final String TOPIC = "product_topics";

    @Autowired
    private KafkaTemplate<String, ProductVariantDTO> kafkaTemplate;

    public void sendProductEvent(ProductVariantDTO event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
