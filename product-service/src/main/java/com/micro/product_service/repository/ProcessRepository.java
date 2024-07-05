package com.micro.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.product_service.models.ProcessRedis;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessRedis, Long>{
    ProcessRedis findByProcessName(String processName);
}
