package com.example.shopapp.category;

import java.util.List;

public interface CategoryService {

    CategoryDto saveCategory(Category category);
    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategoryById(Long id, Category category);
    void deleteCategoryById(Long id);
}
