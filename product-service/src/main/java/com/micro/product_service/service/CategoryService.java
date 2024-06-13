package com.micro.product_service.service;

import java.util.List;
import java.util.Set;

import com.micro.product_service.dto.CategoryDTO;
import com.micro.product_service.models.Category;
import com.micro.product_service.request.CategoryRequest;

public interface CategoryService {
    public Category createCategory(String name, Long parentCategoryId) throws Exception;

    public Category updateCategory(CategoryRequest categoryrRequest, Long categoryId) throws Exception;

    public String deleteCategory(Long categoryId) throws Exception;

    public Category findCategoryById(Long categoryId) throws Exception;

    public List<CategoryDTO> getAllCategory();

    public Set<Long> getAllSubCategoryIds(Long categoryId);
}
