package com.example.shopapp.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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
        List<Long> categoryIds
) {
}
