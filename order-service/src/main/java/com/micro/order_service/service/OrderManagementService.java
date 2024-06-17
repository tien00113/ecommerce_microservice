package com.micro.order_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.models.OrderEvent;
import com.micro.order_service.exception.OrderException;
import com.micro.order_service.models.Order;
import com.micro.order_service.repository.OrderRepository;
import com.micro.order_service.service.kafka.producer.OrderProducer;

@Service
public class OrderManagementService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrderEvent processOrder(OrderEvent stockOrder, OrderEvent paymentOrder){
        try {

            Order order = orderRepository.findByOrderId(stockOrder.getOrderId());

            if (order == null) {
                throw new OrderException("Order not found");
            }

            order.setStockStatus(convertOrderStatus(stockOrder.getStockStatus()));
            order.setPaymentStatus(convertOrderStatus(paymentOrder.getPaymentStatus()));
            orderRepository.save(order);

            // Send notification topic
            // kafkaProducer.sendNotification(order.getOrderNumber(), orderConverter.orderDaoToKafka(order));

            if (order.getStockStatus() == Order.OrderStatus.SUCCESS
                    && order.getPaymentStatus() == Order.OrderStatus.SUCCESS) {
                System.out.println("order successfully ----------------------------------------------------------");
                OrderEvent orderEvent = orderProducer.convertOrderToOrderEvent(order);
                return orderEvent;
            }

            if (order.getStockStatus() == Order.OrderStatus.FAILED
                    && order.getPaymentStatus() == Order.OrderStatus.FAILED) {
                System.out
                        .println("order failed ---------------------------------------------------------------------");
                OrderEvent orderEvent = orderProducer.convertOrderToOrderEvent(order);
                return orderEvent;
            }

            if (order.getStockStatus() == Order.OrderStatus.FAILED) {
                if (order.getPaymentStatus() == Order.OrderStatus.FAILED) {
                    System.out.println("rollback order, failed full -------------------------------");

                    // rollback
                    order.setStockStatus(Order.OrderStatus.ROLLBACK);
                    order.setPaymentStatus(Order.OrderStatus.ROLLBACK);
                }

                System.out.println("rollback order, only stock failed -----------------------------------------");
                // rollback money to customer
                order.setPaymentStatus(Order.OrderStatus.ROLLBACK);

                OrderEvent orderEvent = orderProducer.convertOrderToOrderEvent(order);
                return orderEvent;
            }

            System.out.println("rollback order, only payment failed -----------------------------------------");
            order.setStockStatus(Order.OrderStatus.ROLLBACK);

            OrderEvent orderEvent = orderProducer.convertOrderToOrderEvent(order);
            return orderEvent;

        } catch (OrderException e) {
            System.err.println("Order exception: " + e.getMessage());
            e.printStackTrace();
            // return "{\"error\": \"Order not found\"}";
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            // return "{\"error\": \"Unexpected error occurred\"}";
            return null;
        }
    }

    private Order.OrderStatus convertOrderStatus(OrderEvent.OrderStatus status) {
        return Order.OrderStatus.valueOf(status.name());
    }
}
