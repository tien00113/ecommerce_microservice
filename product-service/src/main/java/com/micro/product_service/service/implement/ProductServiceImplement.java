package com.micro.product_service.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.micro.product_service.models.Product;
import com.micro.product_service.repository.ProductRepo;
import com.micro.product_service.request.ProductFilterRequest;
import com.micro.product_service.service.ProductService;

@Service
public class ProductServiceImplement implements ProductService{

    @Autowired
    private ProductRepo productRepository;

    @Override
    public void createProduct(Product product) throws Exception {
        productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) throws Exception {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new Exception("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setStock(product.getStock());
        existingProduct.setSizes(product.getSizes());
        existingProduct.setActive(product.getActive());
        existingProduct.setImageColorMap(product.getImageColorMap());

        return productRepository.save(existingProduct);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        // Delete the product
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
    public Page<Product> getAllFilter(ProductFilterRequest productFilterRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFilter'");
    }
    
}
