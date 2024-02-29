package com.example.order.dto;

import com.example.address.dto.ResponseAddressDto;
import com.example.orderproduct.dto.OrderProductDto;
import com.example.user.dto.OrderUserDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
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
