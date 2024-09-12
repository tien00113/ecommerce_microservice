package com.micro.product_service.service.redis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.ProcessRedis;
import com.micro.product_service.models.Product;
import com.micro.product_service.process.ProductCacheProcess;
import com.micro.product_service.repository.ProcessRepository;
import com.micro.product_service.repository.ProductRepo;
import com.micro.product_service.request.ProductFilterRequest;

@Service
public class ProductRedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String REDIS_QUEUE_NAME = "productPagesQueue";
    private static final int NUM_THREADS = 10;
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    private String getKey(ProductFilterRequest request) {
        // thêm khoảng giá (them http vào nếu cùng url và params nhưng khác method).
        String key = "products:" + request.getCategoryId() + ":" + request.getPageableDTO().getPage() + ":"
                + request.getPageableDTO().getSize() + ":" + request.getPageableDTO().getSort().getProperty() + ":"
                + request.getPageableDTO().getSort().getDirection();
        return key;
    }

    public Page<ProductDTO> getProductFilter(ProductFilterRequest request) {
        String key = this.getKey(request);

        String json = (String) redisTemplate.opsForValue().get(key);

        if (json != null) {
            try {
                Page<ProductDTO> responsePage = objectMapper.readValue(json, new TypeReference<Page<ProductDTO>>() {
                });
                return responsePage;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveAllProduct(Page<ProductDTO> responsePage, ProductFilterRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAllProduct'");
    }

    public ProductDTO getProductDTO(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            return objectMapper.convertValue(value, ProductDTO.class);
        }
        return null;
    }

    @Scheduled(cron = "0 0 3 * * *") // bắt đầu retry vào 3h sáng
    public void scanFullProducts() {
        executorService.submit(() -> {
            int limit = 10; // Số lượng bản ghi mỗi lần quét
            int page = 0;
            boolean hasMoreRecords = true;

            while (hasMoreRecords) {
                Page<Product> productPage = productRepository.findAll(PageRequest.of(page, limit));

                List<ProductDTO> lists = new ArrayList<>();

                if (productPage.hasContent()) {
                    for (Product product : productPage.getContent()) {
                        ProductDTO productDTO = ProductMapper.toDTO(product);
                        lists.add(productDTO);
                    }
                    cacheService.lPushAll("productPagesQueue", lists);
                    page++;
                } else {
                    break;
                }
            }
            // for (int i = 0; i < NUM_THREADS; i++) {
            //     // executorService.submit(() -> {
            //     // processQueue2();

            //     // });
                ProductCacheProcess process = new ProductCacheProcess(cacheService);
                executorService.execute(process);
            // }
            // setProcessRedis("scanFullProducts", "COMPLETED", LocalDateTime.now(), LocalDateTime.now());
        });
    }

    public void processQueue2() {
        while (true) {
            Object obj = cacheService.rPop(REDIS_QUEUE_NAME);
            if (obj == null) {
                break;
            }
            if (obj instanceof List) {
                List<Object> rawList = (List<Object>) obj;
                try {
                    List<ProductDTO> productDTOs = objectMapper.convertValue(rawList,
                            new TypeReference<List<ProductDTO>>() {
                            });
                    for (ProductDTO productDTO : productDTOs) {
                        String key = "product:_" + productDTO.getId();
                        String key2 = "product:category:_" + productDTO.getCategoryId();
                        cacheService.setValue(key, productDTO);
                        // đẩy hết các product_id có chung category_id lên cùng 1 set
                        cacheService.sPush(key2, productDTO.getId());
                    }
                } catch (Exception e) { 
                    e.printStackTrace();
                    System.out.println("Error converting product JSON: " + e.getMessage());
                }
            }
        }
        setProcessRedis("processQueue", "COMPLETED", LocalDateTime.now(), LocalDateTime.now());
    }

    // @Scheduled(fixedRate = 3000)
    public void checkForUpdates2() {
        if (checkUpdate()) {
            ProcessRedis getTime = processRepository.findByProcessName("checkForUpdates");
            List<Product> updatedProducts = productRepository.findByUpdatedAtAfter(getTime.getLastUpdatedAt());
            for (Product updatedProduct : updatedProducts) {
                ProductDTO productDTO = ProductMapper.toDTO(updatedProduct);
                String key = "product:_" + productDTO.getId();
                String key2 = "product:category:_" + productDTO.getCategoryId();
                cacheService.setValue(key, productDTO);
                cacheService.sPush(key2, productDTO.getId());
                // đấy các sản phẩm liên quan lên cache với 1 list có key là
                // "product:category:_{id}"
                // -> nhược điểm là xuất hiện sản phẩm bị trùng với chính sản phẩm đó
            }

            setProcessRedis("checkForUpdates", "COMPLETED", getTime.getUpdatedAt(), getTime.getUpdatedAt());
        } else {
            return;
        }

    }

    private void setProcessRedis(String processName, String status, LocalDateTime updatedAt,
            LocalDateTime lastUpdatedAt) {
        ProcessRedis processRedis = processRepository.findByProcessName(processName);
        if (processRedis == null) {
            processRedis = new ProcessRedis();
            processRedis.setProcessName(processName);
            processRedis.setUpdatedAt(updatedAt);
            processRedis.setLastUpdatedAt(lastUpdatedAt);
            processRedis.setStatus(status);
        } else {
            processRedis.setStatus(status);
            processRedis.setLastUpdatedAt(lastUpdatedAt);
        }
        processRepository.save(processRedis);
    }

    private boolean checkUpdate() {
        ProcessRedis processRedis = processRepository.findByProcessName("checkForUpdates");
        if (processRedis != null) {
            if (processRedis.getUpdatedAt().isAfter(processRedis.getLastUpdatedAt())) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
