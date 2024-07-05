package com.micro.product_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;
import com.micro.product_service.request.ProductFilterRequest;

public interface ProductService {
    // @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) throws Exception;

    public Product updateProduct(Product product) throws Exception;

    public void checkProductQuantity(Long variantId, int quantity) throws Exception;

    public void rollbackQuantity(Long variantId, int quantity) throws Exception;

    public List<ProductVariant> updateProuductVariants(Long productId, List<ProductVariant> productVariants) throws Exception;

    public int updateProductQuantity(Long variantId, int quantity) throws Exception;
    
    public String deleteProduct(Long productId);

    public List<Product> getAllProduct();

    public Product findProductById(Long productId);

    public ProductVariant findProductVariant(Long productvariantId) throws Exception;
    
    public Page<ProductDTO> getAllFilter(ProductFilterRequest productFilterRequest);

    public void updateProductStock(Product product);

    public Page<ProductDTO> searchProduct(String key, Pageable pageable);
}
