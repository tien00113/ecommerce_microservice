package com.micro.product_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Urls {
    @Value("${order.service.url")
    public static String orderServiceUrl;
}
