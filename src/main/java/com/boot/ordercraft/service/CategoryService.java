package com.boot.ordercraft.service;
 
import com.boot.ordercraft.model.Category;

import com.boot.ordercraft.repository.CategoriesRepository;

import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service

public class CategoryService {

    private final CategoriesRepository categoriesRepository;
 
    public CategoryService(CategoriesRepository categoriesRepository) {

        this.categoriesRepository = categoriesRepository;

    }
 
    public List<Category> getAllCategories() {

        return categoriesRepository.findAll();

    }
 
    public Category getCategoryById(Long id) {

        return categoriesRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

    }
 
    public Category createCategory(Category category) {

        return categoriesRepository.save(category);

    }
 
    public Category updateCategory(Long id, Category updatedCategory) {

        Category existing = getCategoryById(id);

        existing.setCategoryName(updatedCategory.getCategoryName());

        return categoriesRepository.save(existing);

    }
 
    public void deleteCategory(Long id) {

        if (!categoriesRepository.existsById(id)) {

            throw new RuntimeException("Category not found");

        }

        categoriesRepository.deleteById(id);

    }

}

///testing

 