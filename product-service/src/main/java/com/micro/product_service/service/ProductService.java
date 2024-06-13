package com.micro.product_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.models.Product;
import com.micro.product_service.request.ProductFilterRequest;

public interface ProductService {
    // @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(ProductDTO productDTO) throws Exception;

    public Product updateProduct(Product product) throws Exception;
    
    public String deleteProduct(Long productId);

    public List<Product> getAllProduct();

    public Product findProductById(Long productId);
    
    public Page<ProductDTO> getAllFilter(ProductFilterRequest productFilterRequest);

}
