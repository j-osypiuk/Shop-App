package com.example.shopapp.address;

import com.example.shopapp.address.dto.AddressDto;

public interface AddressService {

    AddressDto updateAddressById(Long id, Address address);
}
