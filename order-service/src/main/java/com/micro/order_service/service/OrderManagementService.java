package com.micro.order_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.common.models.OrderEvent;
import com.micro.common.models.OrderEvent.OrderStatus;
import com.micro.order_service.models.Order;
import com.micro.order_service.repository.OrderRepository;

@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepository;

    public Order procesOrder(OrderEvent event){
       
        Order order = orderRepository.findByOrderId(event.getOrderId());

        if (event.getStockStatus() == OrderStatus.SUCCESS && event.getPaymentStatus() == OrderStatus.SUCCESS) {
            order.setStatus(com.micro.order_service.models.Order.OrderStatus.SUCCESS);
            order.setPaymentStatus(com.micro.order_service.models.Order.OrderStatus.SUCCESS);
            order.setStockStatus(com.micro.order_service.models.Order.OrderStatus.SUCCESS);
        } else if(event.getStockStatus() == OrderStatus.FAILED && event.getPaymentStatus() == OrderStatus.FAILED){
            order.setStatus(com.micro.order_service.models.Order.OrderStatus.FAILED);
            order.setPaymentStatus(com.micro.order_service.models.Order.OrderStatus.FAILED);
            order.setStockStatus(com.micro.order_service.models.Order.OrderStatus.FAILED);
        } else if (event.getStockStatus() == OrderStatus.ROLLBACK && event.getPaymentStatus() == OrderStatus.SUCCESS) {
            //do hết hàng
            order.setStatus(com.micro.order_service.models.Order.OrderStatus.ROLLBACK);
            order.setPaymentStatus(com.micro.order_service.models.Order.OrderStatus.SUCCESS);
            order.setStockStatus(com.micro.order_service.models.Order.OrderStatus.FAILED);
            //xử lý refund tiền
        } else {
            // trong trường hợp này là do không truy vấn được product đó để trừ quantity chứ chưa hẳn là hết hàng???
            //xử lý refund tiền
            order.setStatus(com.micro.order_service.models.Order.OrderStatus.ROLLBACK);
            order.setStockStatus(com.micro.order_service.models.Order.OrderStatus.FAILED);
        }
 
        return orderRepository.save(order);
    }

}
