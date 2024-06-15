package com.micro.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.product_service.models.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>{
    
}
