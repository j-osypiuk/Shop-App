package com.example.shopapp.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestAddressDto(
        @NotBlank(message = "Country name cannot be blank")
        @Size(max = 50, message = "Country name cannot contain more than 50 characters")
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*", message = "Country name must start with capital letter and cannot contain numbers")
        String country,
        @NotBlank(message = "Region name cannot be blank")
        @Size(max = 50, message = "Region name cannot contain more than 50 characters")
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*", message = "Region name must start with capital letter and cannot contain numbers")
        String region,
        @NotBlank(message = "City name cannot be blank")
        @Size(max = 50, message = "City name cannot contain more than 50 characters")
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*", message = "City name must start with capital letter and cannot contain numbers")
        String city,
        @NotBlank(message = "Street name cannot be blank")
        @Size(max = 50, message = "Street name cannot contain more than 50 characters")
        @Pattern(regexp = "[A-Z][a-zA-Z\\s]*", message = "Street name must start with capital letter and cannot contain numbers")
        String street,
        @NotBlank(message = "Number name cannot be blank")
        @Size(max = 5, message = "Street number cannot contain more than 5 characters")
        @Pattern(regexp = "[1-9][0-9]*", message = "Street number cannot start with 0 and can contain only digits")
        String number,
        @NotBlank(message = "Postal code name cannot be blank")
        @Size(max = 10, message = "Postal code cannot contain more than 10 characters")
        @Pattern(regexp = "[1-9]*-[0-9]*", message = "Postal code must match regex = [1-9]*-[0-9]*")
        String postalCode
) {
}
