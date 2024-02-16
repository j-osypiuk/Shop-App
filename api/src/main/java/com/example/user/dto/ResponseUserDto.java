package com.example.user.dto;

import com.example.address.dto.ResponseAddressDto;
import com.example.user.Gender;

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
