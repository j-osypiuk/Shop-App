package com.example.shopapp.user.dto;

import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.user.Gender;

import java.time.LocalDateTime;

public record ResponseUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime birthDate,
        Gender gender,
        String phoneNumber,
        ResponseAddressDto address
) {
}
