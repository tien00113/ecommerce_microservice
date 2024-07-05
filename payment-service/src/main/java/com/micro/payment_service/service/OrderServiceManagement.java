package com.micro.payment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.payment_service.service.kafka.producer.PaymentProducer;

@Service
public class OrderServiceManagement {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentProducer paymentProducer;

    public OrderEvent processPaymentOrder(OrderEvent orderEvent) {
        // if (orderEvent.getPaymentStatus() == OrderStatus.NEW) {
        //     String check = paymentService.payment(orderEvent.getOrderId(), orderEvent.getPaymentMethod(),
        //             orderEvent.getPrice());

        //     if (check.equals("error")) {
        //         orderEvent.setPaymentStatus(OrderStatus.FAILED);
        //         System.out.println("Failed Payment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        //     } else {
        //         ///////////////////////không send success nữa mà để PENDING rồi bắn lên topic.
        //         orderEvent.setPaymentStatus(OrderStatus.SUCCESS);
        //         orderEvent.setUrlPayment(check);
        //         System.out.println("SUCCESS Payment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: +" + check);
        //     }

        //     // send to payment topic.
        //     // paymentProducer.sendPaymentTopic(orderEvent.getOrderId(), orderEvent);

        // } else if (orderEvent.getPaymentStatus() == OrderStatus.ROLLBACK) {
        //     paymentService.rollbackPayment(orderEvent);

        //     //// send notification rollback successfully??//////
        //     System.out.println("rollback Payment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        // }
        // return orderEvent;

        return null;
    }
}


