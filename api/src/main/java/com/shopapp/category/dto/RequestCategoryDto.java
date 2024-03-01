package com.shopapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestCategoryDto(
        @NotBlank(message = "Category name cannot be blank")
        @Size(max = 50, message = "Category name cannot contain more than 50 characters")
        String name,
        @NotBlank(message = "Category description cannot be blank")
        String description
) {
}
