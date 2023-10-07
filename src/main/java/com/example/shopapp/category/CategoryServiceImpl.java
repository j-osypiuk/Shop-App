package com.example.shopapp.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(Category category) {
        Category categoryDB = categoryRepository.save(category);
        return CategoryDtoMapper.mapCategoryToCategoryDto(categoryDB);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category categoryDB = categoryRepository.findById(id).get();
        return CategoryDtoMapper.mapCategoryToCategoryDto(categoryDB);
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category categoryDB = categoryRepository.findCategoryByName(name);
        return CategoryDtoMapper.mapCategoryToCategoryDto(categoryDB);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categoriesDB = categoryRepository.findAll();
        return CategoryDtoMapper.mapCategoryListToCategoryDtoList(categoriesDB);
    }

    @Override
    public CategoryDto updateCategoryById(Long id, Category category) {
        Category categoryDB = categoryRepository.findById(id).get();

        if (category.getName() != null && !category.getName().isEmpty()) categoryDB.setName(category.getName());
        if (category.getDescription() != null && !category.getDescription().isEmpty()) categoryDB.setDescription(category.getDescription());

        Category updatedCategory = categoryRepository.save(categoryDB);
        return CategoryDtoMapper.mapCategoryToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
