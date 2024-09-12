package com.micro.product_service.process;

import java.util.Arrays;
import java.util.List;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.service.redis.CacheService;

public class ProductRelatedProcess implements Runnable{

    private CacheService cacheService;
    private final String CACHE_KEY = "product_related_cache_key";

    public ProductRelatedProcess (CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (cacheService.checkExistsKey(CACHE_KEY)) {
                    //b1: lay trong queue
                    ProductDTO customer = (ProductDTO)cacheService.rPop(CACHE_KEY);
                    //b2 Lay ra danh sach id lien quan toi product : cung category
                    List<Integer> relatedProducts = Arrays.asList(1,3,4,5);
                    //b3: luu thong tin len redis cache
                    cacheService.setValue("related_product_"+customer.getId(), relatedProducts);

                } else {
                    Thread.sleep(100);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}