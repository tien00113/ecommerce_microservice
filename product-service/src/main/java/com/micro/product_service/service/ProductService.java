package com.micro.product_service.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.micro.product_service.models.Product;
import com.micro.product_service.request.ProductFilterRequest;

public interface ProductService {
    public void createProduct(Product product) throws Exception;

    public Product updateProduct(Product product) throws Exception;
    
    public String deleteProduct(Long productId);

    public List<Product> getAllProduct();

    public Product findProductById(Long productId);
    
    public Page<Product> getAllFilter(ProductFilterRequest productFilterRequest);

}
