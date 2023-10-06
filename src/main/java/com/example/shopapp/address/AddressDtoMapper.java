package com.example.shopapp.address;

public class AddressDtoMapper {

    public static AddressDto mapAddressToAddressDto(Address address) {
        return new AddressDto(
                address.getAddressId(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getPostalCode()
        );
    }
}
