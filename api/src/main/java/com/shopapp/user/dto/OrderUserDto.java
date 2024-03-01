package com.shopapp.user.dto;

import lombok.Builder;

@Builder
public record OrderUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
