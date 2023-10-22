package com.example.shopapp.address;

import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;

public interface AddressService {

    ResponseAddressDto updateAddressById(Long id, RequestAddressDto requestAddressDto) throws ObjectNotFoundException;
}
