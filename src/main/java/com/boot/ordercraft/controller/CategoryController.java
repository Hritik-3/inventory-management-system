package com.boot.ordercraft.controller;
 
import com.boot.ordercraft.model.Category;

import com.boot.ordercraft.service.CategoryService;

import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@CrossOrigin(origins = "http://localhost:4200")

@RestController

@RequestMapping("/api/categories")

public class CategoryController {

    private final CategoryService categoryService;
 
    public CategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;

    }
 
    @GetMapping

    public List<Category> getAll() {

        return categoryService.getAllCategories();

    }
 
    @GetMapping("/{id}")

    public Category getById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);

    }
 
    @PostMapping

    public Category create(@RequestBody Category category) {

        return categoryService.createCategory(category);

    }
 
    @PutMapping("/{id}")

    public Category update(@PathVariable Long id, @RequestBody Category category) {

        return categoryService.updateCategory(id, category);

    }
 
    @DeleteMapping("/{id}")

    public void delete(@PathVariable Long id) {

        categoryService.deleteCategory(id);

    }

}

 