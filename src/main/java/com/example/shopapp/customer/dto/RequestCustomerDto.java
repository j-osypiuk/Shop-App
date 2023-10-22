package com.example.shopapp.customer.dto;

import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.customer.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record RequestCustomerDto(
        @NotBlank(message = "Customer first name cannot be blank")
        @Size(max = 50, message = "Customer first name cannot contain more than 50 characters")
        String firstName,
        @NotBlank(message = "Customer last name cannot be blank")
        @Size(max = 50, message = "Customer last name cannot contain more than 50 characters")
        String lastName,
        @NotBlank(message = "Customer email cannot be blank")
        @Email(message = "Invalid email pattern")
        @Size(max = 150, message = "Customer email cannot contain more than 150 characters")
        String email,
        @Positive(message = "Customer age must be a positive number")
        @Min(value = 16, message = "Minimal customer age is 16")
        int age,
        Gender gender,
        @Size(max = 10, message = "Customer phone number cannot contain more than 10 digits")
        String phoneNumber,
        @NotNull(message = "Customer address cannot be null")
        @Valid
        RequestAddressDto address
) {
}
