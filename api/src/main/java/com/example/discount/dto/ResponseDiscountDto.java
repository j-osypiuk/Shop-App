package com.example.discount.dto;

public record ResponseDiscountDto(
        Long id,
        String name,
        String description,
        int discountPercent
) {
}
