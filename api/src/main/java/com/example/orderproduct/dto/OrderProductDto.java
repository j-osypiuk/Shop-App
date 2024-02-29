package com.example.orderproduct.dto;

import lombok.Builder;

@Builder
public record OrderProductDto(
        Long productId,
        int amount
) {
}
