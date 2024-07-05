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
import com.micro.product_service.repository.ProcessRepository;
import com.micro.product_service.repository.ProductRepo;
import com.micro.product_service.request.ProductFilterRequest;

import jakarta.transaction.Transactional;

@Service
public class ProductRedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private ProcessRepository processRepository;

    private static final String REDIS_QUEUE_NAME = "productPagesQueue";
    private static final int NUM_THREADS = 5;

    private final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteValue(String key){
        redisTemplate.delete(key);
    }

    public void setTimout(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public void setValueWithTimeout(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkExistsKey(String key) {
        boolean check = false;
        try {
            check = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return check;
    }

    public void lPushAll(String key, List<String> value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sPush(String key, Object value){
        try {
            redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeValueFromSet(String key, Object value) {
        try {
            redisTemplate.opsForSet().remove(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Transactional
    public Object rPop2(String key, long amout) {
        return redisTemplate.opsForList().rightPop(key, amout);
    }

    ///////////////////////////////////////////////
    //////////////////////////////////////////////
    /////////////////////////////////////////////
    private String getKey(ProductFilterRequest request) {
        // thêm khoảng giá (them http vào nếu cùng url và params nhưng khác method).
        String key = "products:" + request.getCategoryId() + ":" + request.getPageableDTO().getPage() + ":"
                + request.getPageableDTO().getSize() + ":" + request.getPageableDTO().getSort().getProperty() + ":"
                + request.getPageableDTO().getSort().getDirection();
        return key;
    }

    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
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
                System.out
                        .println("9898989898988888888888888888888888888888888888888888888999999999999999999999999999");
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
                    this.lPush("productPagesQueue", lists);
                    page++;
                } else {
                    break;
                }

            }
            for (int i = 0; i < NUM_THREADS; i++) {
                System.out.println("running task " + i + "-----------------------");
                executorService.submit(() -> {
                    processQueue2();
                });
            }
            setProcessRedis("scanFullProducts", "COMPLETED", LocalDateTime.now(), LocalDateTime.now());
        });
    }

    public void processQueue() {
        if (checkExistsKey(REDIS_QUEUE_NAME)) {
            for (int i = 0; i < NUM_THREADS; i++) {
                final int taskId = i;
                executorService.submit(() -> {
                    System.out.println("Task " + taskId + " is running");
                    Object obj = this.rPop(REDIS_QUEUE_NAME);
                    while (true) {
                        if (obj instanceof List) {
                            List<Object> rawList = (List<Object>) obj;
                            try {
                                List<ProductDTO> productDTOs = objectMapper.convertValue(rawList,
                                        new TypeReference<List<ProductDTO>>() {
                                        });
                                for (ProductDTO productDTO : productDTOs) {
                                    String key = "product:_" + productDTO.getId();
                                    this.setValue(key, productDTO);
                                    cacheRelatedProducts(productDTO);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Error converting product JSON: " + e.getMessage());
                            }
                        } else {
                            break;
                        }
                    }
                    System.out.println("Task " + taskId + " is done");
                    setProcessRedis("processQueue", "COMPLETED", LocalDateTime.now(), LocalDateTime.now());
                });
            }
        } else {
            return;
        }

    }

    public void processQueue2() {
        while (true) {
            Object obj = this.rPop(REDIS_QUEUE_NAME);
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
                        this.setValue(key, productDTO);
                        // cacheRelatedProducts(productDTO);
                        //đẩy hết các product_id có chung category_id lên cùng 1 set
                        this.sPush(key2, productDTO.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error converting product JSON: " + e.getMessage());
                }
            }
        }
        setProcessRedis("processQueue", "COMPLETED", LocalDateTime.now(), LocalDateTime.now());
    }

    // tạo thêm 1 trường updatedAt trong processRedis-> khi có cập nhật product thì
    // sẽ cập nhật trường updatedAt
    // -> hàm checkforupdate có nhiệm vụ check xem trường updatedAt > lastUpdatedAt
    // của nó hay không?
    // nếu lớn hơn thì thực hiện quét sp mới để đẩy lên cache và cập nhật
    // lastUpdatedAt = updatedAt.
    // @Scheduled(fixedRate = 30000)
    public void checkForUpdates() {
        System.out.println("starting check updates");
        LocalDateTime lastUpdatedAt = getLastUpdatedAt();

        List<Product> updatedProducts = productRepository.findByUpdatedAtAfter(lastUpdatedAt);

        if (!updatedProducts.isEmpty()) {
            LocalDateTime updatedAt = updatedProducts.get(0).getUpdatedAt();

            if (lastUpdatedAt.isBefore(updatedAt)) {
                for (Product updatedProduct : updatedProducts) {
                    if (updatedProduct.getUpdatedAt().isAfter(updatedAt)) {
                        updatedAt = updatedProduct.getUpdatedAt();
                    }
                    ProductDTO productDTO = ProductMapper.toDTO(updatedProduct);
                    String key = "product:_" + productDTO.getId();
                    this.setValue(key, productDTO);
                    System.out.println("process end //////////////////////////////////////");
                    cacheRelatedProducts(productDTO);
                }

                setProcessRedis("checkForUpdates", "COMPLETED", updatedAt, updatedAt);
            } else {
                System.out.println("no product ---- process end //////////////////////////////////////");
                return;
            }
        } else {
            System.out.println("no product last ---- process end //////////////////////////////////////");
            return;
        }
    }

    @Scheduled(fixedRate = 3000)
    public void checkForUpdates2() {
        if (checkUpdate()) {
            ProcessRedis getTime = processRepository.findByProcessName("checkForUpdates");
            List<Product> updatedProducts = productRepository.findByUpdatedAtAfter(getTime.getLastUpdatedAt());
            for (Product updatedProduct : updatedProducts) {
                ProductDTO productDTO = ProductMapper.toDTO(updatedProduct);
                String key = "product:_" + productDTO.getId();
                String key2 = "product:category:_"+productDTO.getCategoryId();
                this.setValue(key, productDTO);
                this.sPush(key2, productDTO.getId());
                //đấy các sản phẩm liên quan lên cache với 1 list có key là "product:category:_{id}"
                // -> nhược điểm là xuất hiện sản phẩm bị trùng với chính sản phẩm đó
                cacheRelatedProducts(productDTO);
            }

            setProcessRedis("checkForUpdates", "COMPLETED", getTime.getUpdatedAt(), getTime.getUpdatedAt());
        } else {
            return;
        }

    }

    private LocalDateTime getLastUpdatedAt() {
        ProcessRedis processRedis = processRepository.findByProcessName("checkForUpdates");
        if (processRedis != null) {
            return processRedis.getLastUpdatedAt();
        } else {
            ProcessRedis processRedis2 = new ProcessRedis();
            processRedis2.setProcessName("checkForUpdates");
            processRedis2.setStatus("RUNNING");
            processRedis2.setLastUpdatedAt(LocalDateTime.now());
            processRedis2.setUpdatedAt(LocalDateTime.now());

            processRepository.save(processRedis2);
        }
        Product topProduct = productRepository.findTopByOrderByUpdatedAtDesc();
        if (topProduct != null) {
            return topProduct.getUpdatedAt();
        }
        return LocalDateTime.MIN;
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

    private void cacheRelatedProducts(ProductDTO productDTO) {
        // Giả định bạn có logic để tìm các sản phẩm liên quan
        List<Long> relatedProductIds = findRelatedProductIds(productDTO);

        String key = "relatedProducts:_" + productDTO.getId();
        this.lPush(key, relatedProductIds);
    }

    private List<Long> findRelatedProductIds(ProductDTO productDTO) {
        // Giả định bạn có logic để tìm các sản phẩm liên quan dựa trên productDTO
        return new ArrayList<>();
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

    // Đảm bảo tắt ExecutorService khi ứng dụng dừng
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
