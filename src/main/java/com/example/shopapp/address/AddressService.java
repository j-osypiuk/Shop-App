package com.example.shopapp.address;

import com.example.shopapp.address.dto.ResponseAddressDto;

public interface AddressService {

    ResponseAddressDto updateAddressById(Long id, Address address);
}
