package com.micro.product_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.Interaction;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;
import com.micro.product_service.repository.InteractionRepository;
import com.micro.product_service.request.ProductFilterRequest;
import com.micro.product_service.service.ProductService;
import com.micro.product_service.service.elasticsearch.ProductSearchService;
import com.micro.product_service.service.redis.CacheService;
import com.micro.product_service.service.redis.ProductRedisService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRedisService productRedisService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping("/public/product")
    public Page<ProductDTO> getAllProducts(
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String color,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "id") String sortProperty,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        ProductFilterRequest filterRequest = new ProductFilterRequest(minPrice, maxPrice, categoryId, color,
                new ProductFilterRequest.PageableDTO(size, page,
                        new ProductFilterRequest.PageableDTO.SortDTO(sortProperty, sortDirection)));
        return productService.getAllFilter(filterRequest);
    }

    @GetMapping("/public/product/{productId}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long productId) {

        String key = "product:_" + productId;
        ProductDTO productDTO = null;

        try {
            productDTO = productRedisService.getProductDTO(key);
            if (productDTO == null) {
                throw new RuntimeException("ProductDTO is null in Redis");
            }
        } catch (Exception e) {
            // Log the error
            log.error("Failed to retrieve product from Redis", e);

            // Fetch from database as fallback
            try {
                productDTO = ProductMapper.toDTO(productService.findProductById(productId));
                // productRedisService.setTimout(key, 10, TimeUnit.MINUTES);
            } catch (Exception ex) {
                // Log the error
                log.error("Failed to retrieve product from database", ex);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        log.info("LOG PRODUCT DETAIL SUCCESSFULLY__________________________________");
        // Interaction interaction = new Interaction();
        // interaction.setProduct(ProductMapper.toEntity(productDTO));
        // interaction.setUserId(2L);
        // interaction.setRating(4);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
        // return new
        // ResponseEntity<>(ProductMapper.toDTO(productService.findProductById(productId)),
        // HttpStatus.OK);
    }

    @GetMapping("/public/product/variant/{productVariantId}")
    public ResponseEntity<ProductVariant> getProductVariant(@PathVariable Long productVariantId) throws Exception {

        return new ResponseEntity<ProductVariant>(productService.findProductVariant(productVariantId), HttpStatus.OK);
    }

    @PostMapping("/private/product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product createdProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(ProductMapper.toDTO(createdProduct), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/private/product/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO)
            throws Exception {
        Product product = ProductMapper.toEntity(productDTO);
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);

        return ResponseEntity.ok(ProductMapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/private/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String message = productService.deleteProduct(id);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @PutMapping("/private/product/{productId}/variants")
    public ResponseEntity<List<ProductVariant>> updateProductVariants(
            @PathVariable Long productId,
            @RequestBody List<ProductVariant> productVariants) {
        try {
            List<ProductVariant> updatedVariants = productService.updateProuductVariants(productId, productVariants);
            return ResponseEntity.ok(updatedVariants);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PatchMapping("/public/product/variants/{variantId}/quantity")
    public ResponseEntity<Void> updateProductQuantity(
            @PathVariable Long variantId,
            @RequestParam int quantity) {
        try {
            productService.updateProductQuantity(variantId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // @PutMapping("/private/product/variants/{variantId}")
    // public ResponseEntity<ProductVariant> updateQuantity(){

    // }

    @PostMapping("/public/test")
    public ResponseEntity<ProductDTO> test(@RequestBody ProductDTO productDTO) {
        // String message = productService.deleteProduct(id);

        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/public/product/search")
    public Page<ProductDTO> searchProduct(@RequestParam String key, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productService.searchProduct(key, pageable);
    }

    @GetMapping("/public/product/search/key")
    public List<ProductDTO> searchProducts(@RequestParam String name) {

        List<ProductDTO> list = productSearchService.searchProducts(name);
        return list;
    }

    @GetMapping("/public/product/related")
    public List<ProductDTO> getRelatedProduct() {
        Set<Object> lists = cacheService.getRandomMembers("product:category:_1", 6);
        List<ProductDTO> listRelated = new ArrayList<>();

        for (Object object : lists) {
            String key = "product:_" + object;
            ProductDTO productDTO = productRedisService.getProductDTO(key);
            listRelated.add(productDTO);
        }
        return listRelated;

    }

    @GetMapping("/public/product/scan")
    public String testScan() {
        productRedisService.scanFullProducts();

        return "thành công";
    }
}
