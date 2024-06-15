package com.micro.product_service.service.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.product_service.models.OrderEvent;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;
import com.micro.product_service.repository.ProductVariantRepository;
import com.micro.product_service.service.ProductService;


@Service
public class OrderConsumer {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductService productService;

    @KafkaListener(topics = "order_topics", groupId = "product-service-group")
    @Transactional
    public void updateStockConsume(OrderEvent event) {
        ProductVariant existingVariant = productVariantRepository.findById(event.getProductVariantId()).orElseThrow(() -> new RuntimeException("product variant not found"));

        if (existingVariant.getQuantity() < event.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        } else {
            existingVariant.setQuantity(existingVariant.getQuantity() - event.getQuantity());

            productVariantRepository.save(existingVariant);

            Product product = existingVariant.getProduct();

            productService.updateProductStock(product);
        }
    }
}
