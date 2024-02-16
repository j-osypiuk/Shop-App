package com.example.discount.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RequestDiscountDto (
        @NotBlank(message = "Discount name cannot be blank")
        @Size(max = 100, message = "Discount name cannot contain more than 100 characters")
        String name,
        @NotBlank(message = "Discount description cannot be blank")
        String description,
        @Positive(message = "Discount percent must be a positive number")
        int discountPercent
){
}
