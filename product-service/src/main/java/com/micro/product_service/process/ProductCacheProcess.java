package com.micro.product_service.process;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.service.redis.CacheService;

public class ProductCacheProcess implements Runnable {
    private CacheService cacheService;
    private final String REDIS_QUEUE_NAME = "productPagesQueue";

    public ProductCacheProcess(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (cacheService.checkExistsKey(REDIS_QUEUE_NAME)) {
                    ProductDTO product = (ProductDTO) cacheService.rPop(REDIS_QUEUE_NAME);
                    cacheService.setValue("product_" + product.getId(), product);
                    cacheService.sPush("product:category:_"+product.getCategoryId(), product.getId());
                } else {
                    // Thread.sleep(100);
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
