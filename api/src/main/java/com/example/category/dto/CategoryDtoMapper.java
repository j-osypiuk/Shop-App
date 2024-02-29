package com.example.category.dto;

import com.example.category.Category;

import java.util.List;

public class CategoryDtoMapper {

    public static ResponseCategoryDto mapCategoryToResponseCategoryDto(Category category) {
        return ResponseCategoryDto.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static Category mapRequestCategoryDtoToCategory(RequestCategoryDto requestCategoryDto) {
        return Category.builder()
                .name(requestCategoryDto.name())
                .description(requestCategoryDto.description())
                .build();
    }

    public static List<ResponseCategoryDto> mapCategoryListToResponseCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryDtoMapper::mapCategoryToResponseCategoryDto)
                .toList();
    }
}
