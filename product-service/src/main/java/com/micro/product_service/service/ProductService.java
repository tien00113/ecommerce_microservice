package com.micro.product_service.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.models.Product;
import com.micro.product_service.models.ProductVariant;
import com.micro.product_service.request.ProductFilterRequest;

public interface ProductService {
    // @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) throws Exception;

    public Product updateProduct(Product product) throws Exception;

    public List<ProductVariant> updateProuductVariants(Long productId, List<ProductVariant> productVariants) throws Exception;

    public void updateProductQuantity(Long variantId, Long quantity) throws Exception;
    
    public String deleteProduct(Long productId);

    public List<Product> getAllProduct();

    public Product findProductById(Long productId);

    public ProductVariant findProductVariant(Long productvariantId) throws Exception;
    
    public Page<ProductDTO> getAllFilter(ProductFilterRequest productFilterRequest);

    public void updateProductStock(Product product);
}
