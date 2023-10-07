package com.example.shopapp.category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDtoMapper {

    public static CategoryDto mapCategoryToCategoryDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getName(),
                category.getDescription()
        );
    }

    public static List<CategoryDto> mapCategoryListToCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryDto(
                        category.getCategoryId(),
                        category.getName(),
                        category.getDescription()
                )).collect(Collectors.toList());
    }
}
