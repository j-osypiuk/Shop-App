package com.example.category;


import com.example.exception.DuplicateUniqueValueException;
import com.example.exception.ObjectNotFoundException;

import java.util.List;

public interface CategoryService {

    Category saveCategory(Category category) throws DuplicateUniqueValueException;
    Category getCategoryById(Long id) throws ObjectNotFoundException;
    Category getCategoryByName(String name) throws ObjectNotFoundException;
    List<Category> getAllCategories() throws ObjectNotFoundException;
    Category updateCategoryById(Long id, Category category) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void deleteCategoryById(Long id) throws ObjectNotFoundException;
}
