package com.example.shopapp.customer.dto;

import com.example.shopapp.address.dto.AddressDto;
import com.example.shopapp.customer.Gender;

public record CustomerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        Gender gender,
        String phoneNumber,
        AddressDto address

) {
}
