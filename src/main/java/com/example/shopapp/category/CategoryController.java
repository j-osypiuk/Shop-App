package com.example.shopapp.category;

import com.example.shopapp.category.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public CategoryDto saveCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping(params = "name")
    public CategoryDto getCategoryByName(@RequestParam("name") String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategoryById(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategoryById(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "Category with id " + id + " deleted successfully";
    }
}
