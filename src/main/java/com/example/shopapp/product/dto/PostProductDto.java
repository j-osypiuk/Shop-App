package com.example.shopapp.product.dto;

import java.util.List;

public record PostProductDto(
        Long id,
        String name,
        String description,
        int amount,
        double price,
        Long discountId,
        List<Long> categoryIds
) {
}
