package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;

import java.util.List;

public interface CategoryService {

    ResponseCategoryDto saveCategory(RequestCategoryDto requestCategoryDto);
    ResponseCategoryDto getCategoryById(Long id);
    ResponseCategoryDto getCategoryByName(String name);
    List<ResponseCategoryDto> getAllCategories();
    ResponseCategoryDto updateCategoryById(Long id, Category category);
    void deleteCategoryById(Long id);
}
