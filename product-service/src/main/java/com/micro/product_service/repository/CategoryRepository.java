package com.micro.product_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.product_service.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // List<Category> findByParentCategoryIsNull();

    List<Category> findByParentId(Long parentId);
}
