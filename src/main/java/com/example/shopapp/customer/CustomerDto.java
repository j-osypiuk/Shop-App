package com.example.shopapp.customer;

import com.example.shopapp.address.AddressDto;

public record CustomerDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        int age,
        Gender gender,
        String phoneNumber,
        AddressDto address

) {
}
