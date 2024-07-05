package com.micro.order_service.service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.order_service.models.ProductEvent;

@Service
public class ProductConsumer {
    // @KafkaListener(topics = "product_topic", groupId = "order-cart-service-group")
    // public void consume(OrderEvent event) {
    //     System.out.println("Received product event: " + event);
    //     // Logic để cập nhật giỏ hàng dựa trên thông tin sản phẩm đã thay đổi
    //     // updateCart(event);
    // }

    // private void updateCart(ProductEvent event) {
    //     // Logic để cập nhật giỏ hàng
    // }
}
