package com.micro.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.payment_service.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    Payment findByOrderId(String orderId);
}
