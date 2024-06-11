package com.micro.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.product_service.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    
}
