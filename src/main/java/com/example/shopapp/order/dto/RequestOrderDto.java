package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.customer.dto.OrderCustomerDto;
import com.example.shopapp.product.dto.OrderProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record RequestOrderDto(
        @Positive(message = "Total price must be a positive number")
        double totalPrice,
        @PositiveOrZero(message = "Total discount cannot be a negative number")
        double totalDiscount,
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
