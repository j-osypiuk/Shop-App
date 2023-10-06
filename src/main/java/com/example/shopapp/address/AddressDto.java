package com.example.shopapp.address;

public record AddressDto(
        Long id,
        String country,
        String region,
        String city,
        String street,
        String number,
        String postalCode
) {
}
