package com.micro.product_service.service.implement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.Category;
import com.micro.product_service.models.Product;
import com.micro.product_service.repository.CategoryRepository;
import com.micro.product_service.repository.ProductRepo;
import com.micro.product_service.request.ProductFilterRequest;
import com.micro.product_service.service.CategoryService;
import com.micro.product_service.service.ProductService;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.MapJoin;

@Service
public class ProductServiceImplement implements ProductService {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    // @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Product product = ProductMapper.toEntity(productDTO);

        Long categoryId = productDTO.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new Exception("Category not found"));

        product.setCategory(category);

        return productRepository.save(product);

    }

    @Override
    public Product updateProduct(Product product) throws Exception {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new Exception("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStock(product.getStock());
        existingProduct.setSizes(product.getSizes());
        existingProduct.setActive(product.getActive());
        existingProduct.setImageColorMap(product.getImageColorMap());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());

        return productRepository.save(existingProduct);
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
        return productRepository.findById(productId).orElse(null);
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
                Sort.by(productFilterRequest.getPageableDTO().getSort().stream()
                        .map(sortDTO -> new Sort.Order(Sort.Direction.fromString(sortDTO.getDirection()),
                                sortDTO.getProperty()))
                        .collect(Collectors.toList())));

        Page<Product> productPage = productRepository.findAll(specification, pageable);

        // Chuyển đổi từ Page<Product> sang Page<ProductDTO>
        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOList, pageable, productPage.getTotalElements());
    }

}