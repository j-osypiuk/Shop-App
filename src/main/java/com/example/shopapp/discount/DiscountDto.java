package com.example.shopapp.discount;

public record DiscountDto(
        Long id,
        String name,
        String description,
        int discountPercent
) {
}
