package com.shopapp.user.dto;

import com.shopapp.address.dto.ResponseAddressDto;
import com.shopapp.user.Gender;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
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
