package com.example.shopapp.category;

import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ResponseCategoryDto> saveCategory(@Valid @RequestBody RequestCategoryDto requestCategoryDto) {
        return new ResponseEntity<>(
                categoryService.saveCategory(requestCategoryDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                categoryService.getCategoryById(id),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "name")
    public ResponseEntity<ResponseCategoryDto> getCategoryByName(@RequestParam("name") String name) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                categoryService.getCategoryByName(name),
                HttpStatus.OK
        );
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ResponseCategoryDto>> getAllCategories() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                categoryService.getAllCategories(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> updateCategoryById(
            @PathVariable Long id, @Valid @RequestBody RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                categoryService.updateCategoryById(id, requestCategoryDto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) throws ObjectNotFoundException {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
