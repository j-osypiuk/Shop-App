package com.shopapp.address;

import com.shopapp.exception.ObjectNotFoundException;

public interface AddressService {

    Address updateAddressById(Long id, Address address) throws ObjectNotFoundException, ObjectNotFoundException;
}
