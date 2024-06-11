package com.micro.product_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.micro.product_service.dto.ProductDTO;
import com.micro.product_service.mapper.ProductMapper;
import com.micro.product_service.models.Product;
import com.micro.product_service.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long productId) {

        return new ResponseEntity<ProductDTO>(ProductMapper.toDTO(productService.findProductById(productId)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) throws Exception {
        Product product = ProductMapper.toEntity(productDTO);
        productService.createProduct(product);

        return new ResponseEntity<ProductDTO>(ProductMapper.toDTO(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws Exception {
        Product product = ProductMapper.toEntity(productDTO);
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);

        return ResponseEntity.ok(ProductMapper.toDTO(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        String message = productService.deleteProduct(id);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

}
