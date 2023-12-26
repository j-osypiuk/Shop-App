package com.example.shopapp.category;

import com.example.shopapp.category.dto.CategoryDtoMapper;
import com.example.shopapp.category.dto.CategoryIdDto;
import com.example.shopapp.category.dto.RequestCategoryDto;
import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CategoryIdDto> saveCategory(@Valid @RequestBody RequestCategoryDto requestCategoryDto) throws DuplicateUniqueValueException {
        Category category = categoryService.saveCategory(CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto));

        return new ResponseEntity<>(
                new CategoryIdDto(category.getCategoryId()),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryService.getCategoryById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "name")
    public ResponseEntity<ResponseCategoryDto> getCategoryByName(@RequestParam("name") String name) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                CategoryDtoMapper.mapCategoryToResponseCategoryDto(categoryService.getCategoryByName(name)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseCategoryDto>> getAllCategories() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                CategoryDtoMapper.mapCategoryListToResponseCategoryDtoList(categoryService.getAllCategories()),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryIdDto> updateCategoryById(@PathVariable Long id, @Valid @RequestBody RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Category category = categoryService.updateCategoryById(
                id,
                CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto)
        );

        return new ResponseEntity<>(
                new CategoryIdDto(category.getCategoryId()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) throws ObjectNotFoundException {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
