package com.micro.order_service.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "order_id")
    private String orderId;

    @PrePersist
    public void generateOrderId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String datePart = now.format(formatter);
        String randomPart = new Random().ints(65, 91)
                .mapToObj(i -> (char) i)
                .limit(6)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        this.orderId = datePart + "CLT" + randomPart;
    }

    private String address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private long totalPrice;
    private String note;

    private LocalDateTime deliveryDateTime;
    private LocalDateTime createAt;

    private LocalDateTime updateStatusAt;

    public enum OrderStatus {
        PLACED, // Đơn hàng đang chờ xử lý
        CONFIRMED, // Đơn hàng đã được xác nhận
        SHIPPED, // Đơn hàng đã được gửi đi
        DELIVERED, // Đơn hàng đã được giao
        CANCELLED // Đơn hàng đã hủy
    }
}