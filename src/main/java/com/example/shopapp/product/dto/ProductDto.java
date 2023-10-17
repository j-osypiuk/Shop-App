package com.example.shopapp.product.dto;

import com.example.shopapp.category.CategoryDto;
import com.example.shopapp.discount.DiscountDto;

import java.util.List;

public record ProductDto(
        Long id,
        String name,
        String description,
        int amount,
        double price,
        DiscountDto discount,
        List<CategoryDto> categories,
        List<String> productPhotos
) {
}
