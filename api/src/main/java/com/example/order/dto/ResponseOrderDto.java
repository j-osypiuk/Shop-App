package com.example.order.dto;

import com.example.address.dto.ResponseAddressDto;
import com.example.orderproduct.dto.OrderProductDto;
import com.example.user.dto.OrderUserDto;

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
