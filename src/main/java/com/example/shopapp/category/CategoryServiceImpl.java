package com.example.shopapp.category;

import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseCategoryDto saveCategory(RequestCategoryDto requestCategoryDto) {
        Category categoryDB = categoryRepository
                .save(CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto));
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public ResponseCategoryDto getCategoryById(Long id) throws ObjectNotFoundException {
        Category categoryDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + id + " not found"));

        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public ResponseCategoryDto getCategoryByName(String name) throws ObjectNotFoundException {
        Category categoryDB = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Category with name = " + name + " not found"));

        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public List<ResponseCategoryDto> getAllCategories() throws ObjectNotFoundException {
        List<Category> categoriesDB = categoryRepository.findAll();

       if (categoriesDB.isEmpty()) throw new ObjectNotFoundException("No categories found");

       return CategoryDtoMapper.mapCategoryListToResponseCategoryDtoList(categoriesDB);
    }

    @Override
    public ResponseCategoryDto updateCategoryById(Long id, RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException {
        Category categoryDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + id + " not found"));

        if (!requestCategoryDto.name().equals(categoryDB.getName()))
            categoryDB.setName(requestCategoryDto.name());
        if (!requestCategoryDto.description().equals(categoryDB.getDescription()))
            categoryDB.setDescription(requestCategoryDto.description());

        categoryRepository.save(categoryDB);
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB);
    }

    @Override
    public void deleteCategoryById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = categoryRepository.deleteCategoryByCategoryId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Category with id = " + id + " not found");
    }
}
