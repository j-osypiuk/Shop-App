package com.example.shopapp.discount.dto;

public record DiscountDto(
        Long id,
        String name,
        String description,
        int discountPercent
) {
}
