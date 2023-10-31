package com.example.shopapp.user.dto;

import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.user.Gender;

public record ResponseUserDto(
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
