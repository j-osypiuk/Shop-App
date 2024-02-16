package com.example.user.dto;

import com.example.user.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PutUserDto(
        @NotBlank(message = "User first name cannot be blank")
        @Size(max = 50, message = "User first name cannot contain more than 50 characters")
        String firstName,
        @NotBlank(message = "User last name cannot be blank")
        @Size(max = 50, message = "User last name cannot contain more than 50 characters")
        String lastName,
        LocalDateTime birthDate,
        Gender gender,
        @Size(max = 10, message = "User phone number cannot contain more than 10 digits")
        String phoneNumber
) {
}
