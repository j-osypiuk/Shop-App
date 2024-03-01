package com.shopapp.category;

import com.shopapp.category.dto.CategoryDtoMapper;
import com.shopapp.category.dto.RequestCategoryDto;
import com.shopapp.category.dto.ResponseCategoryDto;
import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> saveCategory(@Valid @RequestBody RequestCategoryDto requestCategoryDto) throws DuplicateUniqueValueException {
        Category category = categoryService.saveCategory(CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto));

        return new ResponseEntity<>(
                Map.of("categoryId", category.getCategoryId()),
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
    public ResponseEntity<Map<String, Long>> updateCategoryById(@PathVariable Long id, @Valid @RequestBody RequestCategoryDto requestCategoryDto) throws ObjectNotFoundException, DuplicateUniqueValueException {
        Category category = categoryService.updateCategoryById(
                id,
                CategoryDtoMapper.mapRequestCategoryDtoToCategory(requestCategoryDto)
        );

        return new ResponseEntity<>(
                Map.of("categoryId", category.getCategoryId()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) throws ObjectNotFoundException {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
