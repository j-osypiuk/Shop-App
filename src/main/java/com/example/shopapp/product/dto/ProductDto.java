package com.example.shopapp.product.dto;

import com.example.shopapp.category.dto.CategoryDto;
import com.example.shopapp.discount.dto.DiscountDto;

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