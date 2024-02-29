package com.example.address.dto;

import lombok.Builder;

@Builder
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
