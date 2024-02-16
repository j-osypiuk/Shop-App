package com.example.user.dto;

public record OrderUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
