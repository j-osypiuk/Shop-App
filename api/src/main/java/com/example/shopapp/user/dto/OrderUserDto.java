package com.example.shopapp.user.dto;

public record OrderUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
