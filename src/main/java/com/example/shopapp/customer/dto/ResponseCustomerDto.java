package com.example.shopapp.customer.dto;

import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.customer.Gender;

public record ResponseCustomerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        Gender gender,
        String phoneNumber,
        ResponseAddressDto address
) {
}
