package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseCategoryDto saveCategory(RequestCategoryDto requestCategoryDto) {
        Category categoryDB = categoryRepository
                .save(CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto));
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public ResponseCategoryDto getCategoryById(Long id) {
        Optional<Category> categoryDB = categoryRepository.findById(id);
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB.get());
    }

    @Override
    public ResponseCategoryDto getCategoryByName(String name) {
        Category categoryDB = categoryRepository.findCategoryByName(name);
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public List<ResponseCategoryDto> getAllCategories() {
        List<Category> categoriesDB = categoryRepository.findAll();
        return CategoryDtoMapper.mapCategoryListToResponseCategoryDtoList(categoriesDB);
    }

    @Override
    public ResponseCategoryDto updateCategoryById(Long id, Category category) {
        Category categoryDB = categoryRepository.findById(id).get();

        if (category.getName() != null && !category.getName().isEmpty()) categoryDB.setName(category.getName());
        if (category.getDescription() != null && !category.getDescription().isEmpty()) categoryDB.setDescription(category.getDescription());

        Category updatedCategory = categoryRepository.save(categoryDB);
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
