package com.example.address;

import com.example.exception.ObjectNotFoundException;

public interface AddressService {

    Address updateAddressById(Long id, Address address) throws ObjectNotFoundException, ObjectNotFoundException;
}
