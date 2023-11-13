package com.example.shopapp.address;

import com.example.shopapp.exception.ObjectNotFoundException;

public interface AddressService {

    Address updateAddressById(Long id, Address address) throws ObjectNotFoundException;
}
