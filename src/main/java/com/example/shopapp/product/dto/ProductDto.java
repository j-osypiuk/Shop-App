package com.example.shopapp.product.dto;

import com.example.shopapp.category.dto.ResponseCategoryDto;
import com.example.shopapp.discount.dto.ResponseDiscountDto;

import java.util.List;

public record ProductDto(
        Long id,
        String name,
        String description,
        int amount,
        double price,
        ResponseDiscountDto discount,
        List<ResponseCategoryDto> categories,
        List<String> productPhotos
) {
}
