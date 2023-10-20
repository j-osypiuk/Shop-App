package com.example.shopapp.customer.dto;

public record OrderCustomerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
