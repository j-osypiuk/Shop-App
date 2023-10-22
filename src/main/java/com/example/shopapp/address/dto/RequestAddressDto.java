package com.example.shopapp.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestAddressDto(
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*")
        String country,
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*")
        String region,
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*")
        String city,
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*")
        String street,
        @NotBlank
        @Size(max = 5)
        @Pattern(regexp = "[1-9]\\d*")
        String number,
        @NotBlank
        @Size(max = 10)
        @Pattern(regexp = "[1-9]\\d*-\\d*")
        String postalCode
) {
}
