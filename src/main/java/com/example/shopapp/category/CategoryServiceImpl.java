package com.example.shopapp.category;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category saveCategory(Category category) throws DuplicateUniqueValueException {
        if (categoryRepository.existsCategoryByName(category.getName()))
            throw new DuplicateUniqueValueException("Category with name = " + category.getName() + " already exists");

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) throws ObjectNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + id + " not found"));
    }

    @Override
    public Category getCategoryByName(String name) throws ObjectNotFoundException {
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Category with name = " + name + " not found"));
    }

    @Override
    public List<Category> getAllCategories() throws ObjectNotFoundException {
        List<Category> categoriesDB = categoryRepository.findAll();

       if (categoriesDB.isEmpty()) throw new ObjectNotFoundException("No categories found");

       return categoriesDB;
    }

    @Override
    public Category updateCategoryById(Long id, Category category) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Category categoryDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category with id = " + id + " not found"));

        if (categoryRepository.existsCategoryByName(category.getName()))
            throw new DuplicateUniqueValueException("Category with name = " + category.getName() + " already exists");

        if (!category.getName().equals(categoryDB.getName()))
            categoryDB.setName(category.getName());
        if (!category.getDescription().equals(categoryDB.getDescription()))
            categoryDB.setDescription(category.getDescription());

        return categoryRepository.save(categoryDB);
    }

    @Override
    public void deleteCategoryById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = categoryRepository.deleteCategoryById(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Category with id = " + id + " not found");
    }
}
