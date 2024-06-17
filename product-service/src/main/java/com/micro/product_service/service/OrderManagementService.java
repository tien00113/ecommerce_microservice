package com.micro.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.common.models.OrderItemEvent;
import com.micro.product_service.service.kafka.producer.ProductProducer;

@Service
public class OrderManagementService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductProducer productProducer;

    public OrderEvent processStock(OrderEvent orderEvent) throws Exception{
        System.out.println("đang xử lý quantity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List<OrderItemEvent> items = orderEvent.getItems();
        if (orderEvent.getStockStatus() == OrderStatus.NEW) {

            for (OrderItemEvent item: items) {
                Long productVariantId = item.getProductVariantId();
                int quantity = item.getQuantity();

                try {
                    productService.checkProductQuantity(productVariantId, quantity);
                } catch (Exception e) {

                    orderEvent.setStockStatus(OrderEvent.OrderStatus.FAILED);
                    productProducer.sendProductEvent(orderEvent.getOrderId(), orderEvent);
                    System.out.println("đã xử lý quantity (FAILED) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    return orderEvent;
                }
            }

            for (OrderItemEvent item : items) {
                Long productVariantId = item.getProductVariantId();
                int quantity = item.getQuantity();

                try {
                    productService.updateProductQuantity(productVariantId, quantity);
                } catch (Exception e) {
                    // Handle any unexpected exceptions during update
                    orderEvent.setStockStatus(OrderEvent.OrderStatus.FAILED);
                    productProducer.sendProductEvent(orderEvent.getOrderId(), orderEvent);
                    System.out.println("đã xử lý quantity (FAILED) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    return orderEvent;
                }
            }

            orderEvent.setStockStatus(OrderStatus.SUCCESS); 
            productProducer.sendProductEvent(orderEvent.getOrderId(), orderEvent);
            System.out.println("đã xử lý quantity (SUCCESS)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            
        } else if (orderEvent.getStockStatus() == OrderStatus.ROLLBACK) {
            for (OrderItemEvent item : items) {
                productService.rollbackQuantity(item.getProductVariantId(), item.getQuantity());
            }
            System.out.println("đã roll back quantity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return orderEvent;
        }

        System.out.println("đã xử lý quantity >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        return orderEvent;
    }
}
