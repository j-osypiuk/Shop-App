package com.example.address.dto;

import com.example.address.Address;

public class AddressDtoMapper {

    public static ResponseAddressDto mapAddressToResponseAddressDto(Address address) {
        return new ResponseAddressDto(
                address.getAddressId(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostalCode()
        );
    }

    public static Address mapRequestAddressDtoToAddress(RequestAddressDto requestAddressDto){
        return Address.builder()
                .country(requestAddressDto.country())
                .region(requestAddressDto.region())
                .city(requestAddressDto.city())
                .street(requestAddressDto.street())
                .number(requestAddressDto.number())
                .postalCode(requestAddressDto.postalCode())
                .build();
    }
}