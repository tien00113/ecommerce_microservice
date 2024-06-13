package com.micro.product_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.Product;
import com.micro.product_service.request.ProductFilterRequest;
import com.micro.product_service.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/public/product")
    public Page<ProductDTO> getAllProducts(
            @RequestBody ProductFilterRequest productFilterRequest) {
        return productService.getAllFilter(productFilterRequest);
    }

    @GetMapping("/public/product/{productId}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long productId) {

        return new ResponseEntity<ProductDTO>(ProductMapper.toDTO(productService.findProductById(productId)),
                HttpStatus.OK);
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

    @PostMapping("/public/test")
    public ResponseEntity<ProductDTO> test(@RequestBody ProductDTO productDTO) {
        // String message = productService.deleteProduct(id);

        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

}
