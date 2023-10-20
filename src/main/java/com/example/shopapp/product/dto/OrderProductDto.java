package com.example.shopapp.product.dto;

public record OrderProductDto(
        Long id,
        String name,
        double price
) {
}
