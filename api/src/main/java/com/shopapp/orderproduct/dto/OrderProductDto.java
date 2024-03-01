package com.shopapp.orderproduct.dto;

import lombok.Builder;

@Builder
public record OrderProductDto(
        Long productId,
        int amount
) {
}
