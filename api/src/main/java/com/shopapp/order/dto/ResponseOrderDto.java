package com.shopapp.order.dto;

import com.shopapp.address.dto.ResponseAddressDto;
import com.shopapp.orderproduct.dto.OrderProductDto;
import com.shopapp.user.dto.OrderUserDto;
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
