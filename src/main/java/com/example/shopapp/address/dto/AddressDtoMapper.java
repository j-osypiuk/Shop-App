package com.example.shopapp.address.dto;

import com.example.shopapp.address.Address;

public class AddressDtoMapper {

    public static ResponseAddressDto mapAddressToAddressDto(Address address) {
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
