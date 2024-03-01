package com.shopapp.discount.dto;

import lombok.Builder;

@Builder
public record ResponseDiscountDto(
        Long id,
        String name,
        String description,
        int discountPercent
) {
}
