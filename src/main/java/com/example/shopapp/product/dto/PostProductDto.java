package com.example.shopapp.product.dto;

import com.example.shopapp.discount.DiscountDto;

import java.util.List;

public record PostProductDto(
        Long id,
        String name,
        String description,
        int amount,
        double price,
        DiscountDto discount,
        List<Long> categoryIds
) {
}
