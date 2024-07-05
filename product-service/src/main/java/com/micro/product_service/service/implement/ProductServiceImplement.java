package com.micro.product_service.service.implement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.micro.common.models.OrderEvent;
import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.Category;
import com.micro.product_service.models.ProcessRedis;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;
import com.micro.product_service.repository.CategoryRepository;
import com.micro.product_service.repository.ProcessRepository;
import com.micro.product_service.repository.ProductElasticsearchRepository;
import com.micro.product_service.repository.ProductRepo;
import com.micro.product_service.repository.ProductVariantRepository;
import com.micro.product_service.request.ProductFilterRequest;
import com.micro.product_service.service.CategoryService;
import com.micro.product_service.service.ProductService;
import com.micro.product_service.service.redis.ProductRedisService;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;

@Service
public class ProductServiceImplement implements ProductService {

    private static final String TOPIC = "product_topic";

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProductRedisService productRedisService;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Product product = ProductMapper.toEntity(productDTO);

        Long categoryId = productDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("Category not found"));

        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> {
                variant.setCreatedAt(LocalDateTime.now());
                variant.setUpdatedAt(LocalDateTime.now());
            });
        }

        Product createdProduct = productRepository.save(product);

        String key = "product:_" + createdProduct.getId();
        ProductDTO productDTO2 = ProductMapper.toDTO(createdProduct);
        productRedisService.setValue(key, productDTO2);

        productDTO2.setCreatedAt(null);
        productDTO2.setUpdatedAt(null);
        if (productDTO2.getVariants() != null) {
            productDTO2.getVariants().forEach(variant -> {
                variant.setCreatedAt(null);
                variant.setUpdatedAt(null);
            });
        }
        productElasticsearchRepository.save(productDTO2);
        
        return createdProduct;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(Product product) throws Exception {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new Exception("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStock(product.getStock());
        existingProduct.setActive(product.getActive());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setUpdatedAt(LocalDateTime.now());

        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category newCategory = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new Exception("Category not found"));
            String key = "product:category:_" + existingProduct.getCategory().getId();

            productRedisService.removeValueFromSet(key, existingProduct.getId());

            existingProduct.setCategory(newCategory);
        }
        if (existingProduct.getVariants() != null) {
            existingProduct.getVariants().forEach(variant -> variant.setUpdatedAt(LocalDateTime.now()));
        }

        ProcessRedis processRedis = processRepository.findByProcessName("checkForUpdates");

        processRedis.setUpdatedAt(existingProduct.getUpdatedAt());

        processRepository.save(processRedis);
        return productRepository.save(existingProduct);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductVariant> updateProuductVariants(Long productId, List<ProductVariant> productVariants)
            throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));

        List<ProductVariant> existingVariants = existingProduct.getVariants();

        Map<Long, ProductVariant> existingVariantMap = existingVariants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, variant -> variant));

        for (ProductVariant newVariant : productVariants) {
            newVariant.setUpdatedAt(LocalDateTime.now());
            if (newVariant.getId() != null && existingVariantMap.containsKey(newVariant.getId())) {

                ProductVariant existingVariant = existingVariantMap.get(newVariant.getId());
                existingVariant.setColor(newVariant.getColor());
                existingVariant.setSize(newVariant.getSize());
                existingVariant.setQuantity(newVariant.getQuantity());
                existingVariant.setImageUrl(newVariant.getImageUrl());
                existingVariant.setUpdatedAt(LocalDateTime.now());
            } else {
                newVariant.setProduct(existingProduct);
                existingVariants.add(newVariant);
            }
        }

        existingVariants.removeIf(existingVariant -> productVariants.stream().noneMatch(
                newVariant -> newVariant.getId() != null && newVariant.getId().equals(existingVariant.getId())));

        updateProductStock(existingProduct);

        LocalDateTime latestUpdatedAt = existingVariants.stream()
                .map(ProductVariant::getUpdatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
        existingProduct.setUpdatedAt(latestUpdatedAt);

        ProcessRedis processRedis = processRepository.findByProcessName("checkForUpdates");

        processRedis.setUpdatedAt(existingProduct.getUpdatedAt());

        processRepository.save(processRedis);

        productRepository.save(existingProduct);

        return existingProduct.getVariants();
    }

    @Override
    public void checkProductQuantity(Long variantId, int quantity) throws Exception {
        ProductVariant existingVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new Exception("ProductVariant not found"));

        if (existingVariant.getQuantity() < quantity) {
            throw new Exception("Not enough stock available for ProductVariant ID " + variantId);
        }
    }

    @Override
    public int updateProductQuantity(Long variantId, int quantity) throws Exception {
        ProductVariant existingVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new Exception("ProductVariant not found"));

        if (existingVariant.getQuantity() < quantity) {
            return 0;
        }

        existingVariant.setQuantity(existingVariant.getQuantity() - quantity);
        // existingVariant.setQuantity(100);
        productVariantRepository.save(existingVariant);

        // Update stock of the product
        Product product = existingVariant.getProduct();
        updateProductStock(product);

        return 1;
    }

    @Override
    public void updateProductStock(Product product) {
        long totalStock = product.getVariants().stream()
                .mapToLong(ProductVariant::getQuantity)
                .sum();
        product.setStock(totalStock);
        productRepository.save(product);
    }

    @Override
    public void rollbackQuantity(Long variantId, int quantity) throws Exception {
        ProductVariant existingVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new Exception("ProductVariant not found"));

        existingVariant.setQuantity(quantity);

        productVariantRepository.save(existingVariant);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(existingProduct);

        return "Product deleted successfully";
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with"));
    }

    @Override
    public Page<ProductDTO> getAllFilter(ProductFilterRequest productFilterRequest) {
        Specification<Product> specification = Specification.where(null);

        if (productFilterRequest.getMinPrice() != 0) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("price"), productFilterRequest.getMinPrice()));
        }
        if (productFilterRequest.getMaxPrice() != 0) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .lessThanOrEqualTo(root.get("price"), productFilterRequest.getMaxPrice()));
        }

        if (productFilterRequest.getColor() != null && !productFilterRequest.getColor().isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                MapJoin<Product, String, String> join = root.joinMap("imageColorMap", JoinType.INNER);
                return criteriaBuilder.like(join.key(), "%" + productFilterRequest.getColor() + "%");
            });
        }

        if (productFilterRequest.getCategoryId() != null) {
            Set<Long> categoryIds = categoryService.getAllSubCategoryIds(productFilterRequest.getCategoryId());
            specification = specification
                    .and((root, query, criteriaBuilder) -> root.get("category").get("id").in(categoryIds));
        }

        Pageable pageable = PageRequest.of(
                productFilterRequest.getPageableDTO().getPage(),
                productFilterRequest.getPageableDTO().getSize(),
                Sort.by(new Sort.Order(
                        Sort.Direction.fromString(productFilterRequest.getPageableDTO().getSort().getDirection()),
                        productFilterRequest.getPageableDTO().getSort().getProperty())));

        Page<Product> productPage = productRepository.findAll(specification, pageable);

        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
    }

    // public void sendProductTopic(ProductVariantDTO productVariantDTO){
    // // kafkaTemplate.send(TOPIC, productVariantDTO);
    // }

    @Override
    public ProductVariant findProductVariant(Long productvariantId) throws Exception {
        return productVariantRepository.findById(productvariantId)
                .orElseThrow(() -> new Exception("Product Variant not found"));
    }

    @Override
    @Cacheable(value = "products", key = "#name + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductDTO> searchProduct(String key, Pageable pageable) {
        // return productElasticsearchRepository.findByNameContaining(key, pageable);
        return null;
    }
}
