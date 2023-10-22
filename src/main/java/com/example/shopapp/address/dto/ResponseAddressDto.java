package com.example.shopapp.address.dto;

public record ResponseAddressDto(
        Long id,
        String country,
        String region,
        String city,
        String street,
        String number,
        String postalCode
) {
}
