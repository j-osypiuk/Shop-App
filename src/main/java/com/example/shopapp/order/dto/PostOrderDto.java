package com.example.shopapp.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostOrderDto(
        Long id,
        LocalDateTime orderDate,
        double totalPrice,
        double totalDiscount,
        boolean isCompleted,
        Long customerId,
        Long addressId,
        List<Long> productsId
) {
}
