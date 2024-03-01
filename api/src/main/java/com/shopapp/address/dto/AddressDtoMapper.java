package com.shopapp.address.dto;

import com.shopapp.address.Address;

public class AddressDtoMapper {

    public static ResponseAddressDto mapAddressToResponseAddressDto(Address address) {
        return ResponseAddressDto.builder()
                .id(address.getAddressId())
                .country(address.getCountry())
                .region(address.getRegion())
                .city(address.getCity())
                .street(address.getStreet())
                .number(address.getNumber())
                .postalCode(address.getPostalCode())
                .build();
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
