package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseCategoryDto saveCategory(@Valid @RequestBody RequestCategoryDto requestCategoryDto) {
        return categoryService.saveCategory(requestCategoryDto);
    }

    @GetMapping("/{id}")
    public ResponseCategoryDto getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping(params = "name")
    public ResponseCategoryDto getCategoryByName(@RequestParam("name") String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping
    public List<ResponseCategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/{id}")
    public ResponseCategoryDto updateCategoryById(@PathVariable Long id, @Valid @RequestBody Category category) {
        return categoryService.updateCategoryById(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "Category with id " + id + " deleted successfully";
    }
}
