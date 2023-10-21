package com.example.shopapp.order.dto;

import com.example.shopapp.address.dto.AddressDto;
import com.example.shopapp.customer.dto.OrderCustomerDto;
import com.example.shopapp.product.dto.OrderProductDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        LocalDateTime orderDate,
        double totalPrice,
        double totalDiscount,
        boolean isCompleted,
        OrderCustomerDto customer,
        AddressDto address,
        List<OrderProductDto> products
) {
}
