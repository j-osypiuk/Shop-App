package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.user.dto.OrderUserDto;
import com.example.shopapp.product.dto.OrderProductDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseOrderDto(
        Long id,
        LocalDateTime orderDate,
        double totalPrice,
        double totalDiscount,
        boolean isCompleted,
        OrderUserDto user,
        ResponseAddressDto address,
        List<OrderProductDto> products
) {
}
