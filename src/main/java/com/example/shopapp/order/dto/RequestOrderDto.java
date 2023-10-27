package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.RequestAddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestOrderDto(
        boolean isCompleted,
        @NotNull(message = "Order must contain customer")
        Long customerId,
        @NotNull(message = "Order must contain address")
        @Valid
        RequestAddressDto address,
        @NotEmpty(message = "Order must contain at least one product")
        List<Long> productIds
) {
}
