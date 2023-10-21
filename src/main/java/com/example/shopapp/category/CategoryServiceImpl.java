package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
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
    public ResponseCategoryDto getCategoryById(Long id) throws ObjectNotFoundException {
        Optional<Category> categoryDB = categoryRepository.findById(id);

        if (categoryDB.isEmpty()) throw new ObjectNotFoundException("Category with id = " + id + " not found");

        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB.get());
    }

    @Override
    public ResponseCategoryDto getCategoryByName(String name) throws ObjectNotFoundException {
        Optional<Category> categoryDB = categoryRepository.findCategoryByName(name);

        if (categoryDB.isEmpty()) throw new ObjectNotFoundException("Category with name = " + name + " not found");

        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryDB.get());
    }

    @Override
    public List<ResponseCategoryDto> getAllCategories() throws ObjectNotFoundException {
        List<Category> categoriesDB = categoryRepository.findAll();

       if (categoriesDB.isEmpty()) throw new ObjectNotFoundException("No categories found");

       return CategoryDtoMapper.mapCategoryListToResponseCategoryDtoList(categoriesDB);
    }

    @Override
    public ResponseCategoryDto updateCategoryById(Long id, RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException {
        Optional<Category> categoryDB = categoryRepository.findById(id);

        if (categoryDB.isEmpty()) throw new ObjectNotFoundException("Category with id = " + id + " not found");

        if (!requestCategoryDto.name().equals(categoryDB.get().getName()))
            categoryDB.get().setName(requestCategoryDto.name());
        if (!requestCategoryDto.description().equals(categoryDB.get().getDescription()))
            categoryDB.get().setDescription(requestCategoryDto.description());

        Category updatedCategory = categoryRepository.save(categoryDB.get());
        return CategoryDtoMapper.mapCategoryToResponseCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategoryById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = categoryRepository.deleteCategoryByCategoryId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Category with id = " + id + " not found");
    }
}
