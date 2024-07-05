package com.micro.notification_service.service.kafka.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.notification_service.service.SendEmailService;

import jakarta.mail.MessagingException;

@Service
public class OrderConsumer {

    @Autowired
    private SendEmailService sendEmailService;

    @KafkaListener(topics = "product_topic", groupId = "order-group")
    public void listenNotifi(OrderEvent event) {
        if (event.getStockStatus() == OrderStatus.SUCCESS && event.getPaymentStatus() == OrderStatus.SUCCESS) {
            try {
                //call api lấy email với event.getUserId()
                sendEmailService.sendHtmlEmail("tiennguyenhienvx@gmail.com", "<T-SHOP> Đơn Hàng Thành Công",
                        event);

            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (event.getStockStatus() == OrderStatus.ROLLBACK && event.getPaymentStatus() == OrderStatus.SUCCESS || event.getStockStatus() == OrderStatus.FAILED && event.getPaymentStatus() == OrderStatus.SUCCESS) {
            try {
                sendEmailService.sendMailRefund("tiennguyenhienvx@gmail.com", event);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
