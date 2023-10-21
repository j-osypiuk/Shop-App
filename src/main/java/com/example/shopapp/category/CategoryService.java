package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;

import java.util.List;

public interface CategoryService {

    ResponseCategoryDto saveCategory(RequestCategoryDto requestCategoryDto);
    ResponseCategoryDto getCategoryById(Long id) throws ObjectNotFoundException;
    ResponseCategoryDto getCategoryByName(String name) throws ObjectNotFoundException;
    List<ResponseCategoryDto> getAllCategories() throws ObjectNotFoundException;
    ResponseCategoryDto updateCategoryById(Long id, RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException;
    void deleteCategoryById(Long id) throws ObjectNotFoundException;
}
