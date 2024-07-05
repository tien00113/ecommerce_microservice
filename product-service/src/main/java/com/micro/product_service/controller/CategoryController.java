package com.micro.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.micro.product_service.dto.CategoryDTO;
import com.micro.product_service.models.Category;
import com.micro.product_service.request.CategoryRequest;
import com.micro.product_service.service.CategoryService;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/category")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategory();
    }

    @PostMapping("/private/category")
    public ResponseEntity<Category> creatCategory(@RequestBody CategoryRequest categoryRequest, @RequestHeader("Authorization") String jwt) throws Exception{

        Category createdCategory = categoryService.createCategory(categoryRequest.getName(), categoryRequest.getParentCategoryId());

        return new ResponseEntity<Category>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/private/category/{categoryId}")
    public Category updatCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Long categoryId) throws Exception{

        Category updatedCategory = categoryService.updateCategory(categoryRequest, categoryId);

        return updatedCategory;
    }

    @DeleteMapping("/private/category/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) throws Exception{

        String message = categoryService.deleteCategory(categoryId);

        return message;
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<Category> getCategoryDetail(@PathVariable Long categoryId) throws Exception{
        return ResponseEntity.ok(categoryService.findCategoryById(categoryId));
    }

}
