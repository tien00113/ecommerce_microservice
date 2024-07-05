package com.micro.payment_service.service.implement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.PaymentRequest;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.payment_service.enums.BankType;
import com.micro.payment_service.factory.PaymentFactory;
import com.micro.payment_service.models.Payment;
import com.micro.payment_service.models.PaymentMethod;
import com.micro.payment_service.repository.PaymentRepository;
import com.micro.payment_service.service.PaymentService;
import com.micro.payment_service.service.kafka.producer.PaymentProducer;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaymentServiceImplement implements PaymentService {
    @Autowired
    private PaymentMethod vnPayService;

    @Autowired
    private PaymentProducer paymentProducer;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentRequest payment(PaymentRequest request) {

        BankType bankType;
        try {
            bankType = BankType.valueOf(request.getMethod());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported bank type: " + request.getMethod());
        }

        PaymentMethod paymentMethod = PaymentFactory.getPaymentMethod(bankType);

        Payment payment = new Payment();

        payment.setAmount(request.getPrice() / 100);
        payment.setOrderId(request.getOrderId());
        // payment.setCreateAt(LocalDateTime.now());
        // payment.setCode(transactionId);
        payment.setStartPayment(LocalDateTime.now());
        payment.setMethod(request.getMethod());
        payment.setStatus("PENDING");

        paymentRepository.save(payment);

        PaymentRequest paymentUrl = paymentMethod.processPaymentMethod(request.getPrice(), request.getOrderId(),
                request.getUserId(), request.getEmail());

        return paymentUrl;

    }

    @Override
    public boolean rollbackPayment(OrderEvent orderEvent) {
        System.out
                .println("Rollback money successfully $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        return true;
    }

    @Override
    public Map<String, String> paymentReturn(HttpServletRequest request) {
        String orderInfo = request.getParameter("vnp_OrderInfo");

        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        int stt = vnPayService.orderReturn(request);

        Payment payment = paymentRepository.findByOrderId(orderInfo);

        payment.setCreateAt(paymentTime);
        payment.setCode(transactionId);

        OrderEvent event = new OrderEvent();
        if (stt == 1) {
            event.setPaymentStatus(OrderStatus.SUCCESS);
            payment.setStatus("SUCCESS");
        } else {
            event.setPaymentStatus(OrderStatus.FAILED);
            payment.setStatus("FAILED");
        }
        String key = request.getParameter("vnp_OrderInfo");
        event.setOrderId(key);
        // send to payment topic
        paymentRepository.save(payment);
        paymentProducer.sendPaymentTopic(key, event);

        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderInfo);
        response.put("totalPrice", totalPrice);
        response.put("paymentTime", paymentTime);
        response.put("transactionId", transactionId);
        response.put("paymentStatus", stt == 1 ? "SUCCESS" : "FAILED");

        return response;
    }

}
