package com.micro.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.product_service.models.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    
}
