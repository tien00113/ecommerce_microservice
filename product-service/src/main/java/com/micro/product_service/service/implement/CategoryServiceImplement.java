package com.micro.product_service.service.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.micro.product_service.dto.CategoryDTO;
import com.micro.product_service.models.Category;
import com.micro.product_service.repository.CategoryRepository;
import com.micro.product_service.request.CategoryRequest;
import com.micro.product_service.service.CategoryService;

@Service
public class CategoryServiceImplement implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> rootCategories = categoryRepository.findAll();
        return rootCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToDTO(Category category) {
        List<Category> subCategories = categoryRepository.findByParentId(category.getId());
        List<CategoryDTO> subCategoryDTOs = subCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getLevel(),
                subCategoryDTOs);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(String name, Long parentId) throws Exception {
        Category parentCategory = null;
        int level = 1;

        if (parentId != null) {
            Optional<Category> parentCategoryOptional = categoryRepository.findById(parentId);
            if (parentCategoryOptional.isPresent() && parentCategoryOptional.get().getLevel() < 3) {
                parentCategory = parentCategoryOptional.get();
                level = parentCategory.getLevel() + 1;

                Category category = new Category();
                category.setName(name);
                category.setParentId(Long.valueOf(parentId));
                category.setLevel(level);

                return categoryRepository.save(category);
            } else {
                throw new IllegalArgumentException("Parent category ID not found or Level is not support: " + parentId);
            }
        } else {
            Category category = new Category();
            category.setName(name);
            category.setLevel(level);
            // category.setParentId();

            return categoryRepository.save(category);

        }

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(CategoryRequest categoryRequest, Long categoryId) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);

        if (existingCategory.isEmpty()) {
            throw new Exception("Category not found with ID: " + categoryId);
        }

        Category updatedCategory = existingCategory.get();
        updatedCategory.setName(categoryRequest.getName());
        // updatedCategory.setParentId(categoryId);
        // updatedCategory.setLevel(category.getLevel());

        if (categoryRequest.getParentCategoryId() != null) {
            Optional<Category> existingParentCategory = categoryRepository
                    .findById(categoryRequest.getParentCategoryId());
            if (existingParentCategory.isEmpty()) {
                throw new Exception("Category parent not found with ID: " + categoryRequest.getParentCategoryId());
            } else {
                if (existingParentCategory.get().getLevel() < 3) {
                    updatedCategory.setParentId(categoryRequest.getParentCategoryId());
                    updatedCategory.setLevel(existingParentCategory.get().getLevel() + 1);
                } else {
                    throw new Exception("Currently only supports 3-level category");
                }
            }
        }

        return categoryRepository.save(updatedCategory);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategory(Long categoryId) throws Exception {
        Optional<Category> existingCategory = categoryRepository.findById(categoryId);

        if (existingCategory.isEmpty()) {
            throw new Exception("Category not found with ID: " + categoryId);
        }

        categoryRepository.deleteById(categoryId);
        return "Category with ID: " + categoryId + " has been deleted successfully.";
    }

    @Override
    public Category findCategoryById(Long categoryId) throws Exception {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new Exception("Category not found with ID: " + categoryId);
        }
    }

    @Override
    public Set<Long> getAllSubCategoryIds(Long categoryId) {
        Set<Long> categoryIds = new HashSet<>();
        addSubCategoryIds(categoryId, categoryIds);
        return categoryIds;
    }

    private void addSubCategoryIds(Long categoryId, Set<Long> categoryIds) {
        categoryIds.add(categoryId);
        List<Category> subCategories = categoryRepository.findByParentId(categoryId);
        for (Category subCategory : subCategories) {
            addSubCategoryIds(subCategory.getId(), categoryIds);
        }
    }
}
