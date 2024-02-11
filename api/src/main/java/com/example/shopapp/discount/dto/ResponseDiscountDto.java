package com.example.shopapp.discount.dto;

public record ResponseDiscountDto(
        Long id,
        String name,
        String description,
        int discountPercent
) {
}
