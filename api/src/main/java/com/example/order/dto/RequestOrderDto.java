package com.example.order.dto;

import com.example.address.dto.RequestAddressDto;
import com.example.orderproduct.dto.OrderProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestOrderDto(
        boolean isCompleted,
        @NotNull(message = "Order must contain address")
        @Valid
        RequestAddressDto address,
        @NotEmpty(message = "Order must contain at least one product")
        List<OrderProductDto> orderProducts
) {
}
