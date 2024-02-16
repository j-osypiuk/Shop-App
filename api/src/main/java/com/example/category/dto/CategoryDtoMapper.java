package com.example.category.dto;

import com.example.category.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDtoMapper {

    public static ResponseCategoryDto mapCategoryToResponseCategoryDto(Category category) {
        return new ResponseCategoryDto(
                category.getCategoryId(),
                category.getName(),
                category.getDescription()
        );
    }

    public static Category mapRequestCategoryDtoToCategory(RequestCategoryDto requestCategoryDto) {
        return Category.builder()
                .name(requestCategoryDto.name())
                .description(requestCategoryDto.description())
                .build();
    }

    public static List<ResponseCategoryDto> mapCategoryListToResponseCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(category -> new ResponseCategoryDto(
                        category.getCategoryId(),
                        category.getName(),
                        category.getDescription()
                )).collect(Collectors.toList());
    }
}
