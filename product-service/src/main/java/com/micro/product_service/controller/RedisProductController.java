package com.micro.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.product_service.service.redis.ProductRedisService;

@RestController
public class RedisProductController {

    @Autowired
    private ProductRedisService productRedisService;


    


    // @GetMapping("/public/product/redis")
    // public String testRedis(){
    //     List<String> tt = new ArrayList<>();

    //     tt.add("A");
    //     tt.add("B");
    //     tt.add("C");

    //     ProductDTO productDTO = new ProductDTO();
    //     productDTO.setName("haiahisad");

    //     productRedisService.setValue("testqg", tt);

    //     return "abc";
    // }

    @GetMapping("/public/product/redis/scan")
    public String testScan(){

        productRedisService.scanFullProducts();

        return "thành công";
    }

    // @GetMapping("/public/product/redis/process")
    // public String testprocess(){
    //     productRedisService.processQueue();

    //     return "đã hoàn thành";
    // }

}
