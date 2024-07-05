package com.micro.product_service.service.kafka.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.common.models.OrderItemEvent;
import com.micro.product_service.service.ProductService;
import com.micro.product_service.service.client.OrderClient;
import com.micro.product_service.service.kafka.producer.ProductProducer;

@Service
public class OrderConsumer {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ProductProducer productProducer;

    @KafkaListener(topics = "payment_topic", groupId = "payment-group")
    public void updateStock(OrderEvent event) {
        boolean hasOutOfStockItem = false;

        if (event.getPaymentStatus() == OrderStatus.SUCCESS) {
            List<OrderItemEvent> items = orderClient.getItemEvents(event.getOrderId());
            for (OrderItemEvent item : items) {
                Long productVariantId = item.getProductVariantId();
                int quantity = item.getQuantity();
                try {
                    Integer stt = productService.updateProductQuantity(productVariantId, quantity);

                    if (stt == 0) {
                        hasOutOfStockItem = true;
                        break;
                    }
                } catch (Exception e) {
                    event.setStockStatus(OrderStatus.FAILED);
                    productProducer.sendProductEvent(event.getOrderId(), event);
                    return;
                }
            }
            if (hasOutOfStockItem) {
                event.setStockStatus(OrderStatus.ROLLBACK);
                productProducer.sendProductEvent(event.getOrderId(), event);
            } else {
                event.setStockStatus(OrderStatus.SUCCESS);
                productProducer.sendProductEvent(event.getOrderId(), event);
            }
        } else if (event.getPaymentStatus() == OrderStatus.FAILED) {
            event.setStockStatus(OrderStatus.FAILED);
            productProducer.sendProductEvent(event.getOrderId(), event);
        }
    }
}
