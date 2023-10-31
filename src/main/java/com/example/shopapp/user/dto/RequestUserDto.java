package com.example.shopapp.user.dto;

import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.user.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record RequestUserDto(
        @NotBlank(message = "User first name cannot be blank")
        @Size(max = 50, message = "User first name cannot contain more than 50 characters")
        String firstName,
        @NotBlank(message = "User last name cannot be blank")
        @Size(max = 50, message = "User last name cannot contain more than 50 characters")
        String lastName,
        @NotBlank(message = "User email cannot be blank")
        @Email(message = "Invalid email pattern")
        @Size(max = 100, message = "User email cannot contain more than 100 characters")
        String email,
        @NotBlank(message = "User password cannot be blank")
        @Size(max = 100, message = "User password cannot contain more than 100 characters")
        String password,
        @Positive(message = "User age must be a positive number")
        @Min(value = 16, message = "Minimal User age is 16")
        int age,
        Gender gender,
        @Size(max = 10, message = "User phone number cannot contain more than 10 digits")
        String phoneNumber,
        @NotNull(message = "User address cannot be null")
        @Valid
        RequestAddressDto address
) {
}
