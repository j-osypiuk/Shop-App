package com.example.product.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record RequestProductDto(
        @NotBlank(message = "Product name cannot be blank")
        @Size(max = 50, message = "Product name cannot contain more than 50 characters")
        String name,
        @NotBlank(message = "Product description cannot be blank")
        String description,
        @PositiveOrZero
        int amount,
        @Positive
        double price,
        Long discountId,
        @NotEmpty(message = "Product must belongs to at least one category")
        List<Long> categoryIds
) {
}
