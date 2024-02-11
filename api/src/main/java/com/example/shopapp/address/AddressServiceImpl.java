package com.example.shopapp.address;

import com.example.shopapp.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address updateAddressById(Long id, Address address) throws ObjectNotFoundException {
        Address addressDB = addressRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Address with id = " + id + " not found"));

        if (!address.getCountry().equals(addressDB.getCountry()))
            addressDB.setCountry(address.getCountry());
        if (!address.getRegion().equals(addressDB.getRegion()))
            addressDB.setRegion(address.getRegion());
        if (!address.getCity().equals(addressDB.getCity()))
            addressDB.setCity(address.getCity());
        if (!address.getStreet().equals(addressDB.getStreet()))
            addressDB.setStreet(address.getStreet());
        if (!address.getNumber().equals(addressDB.getNumber()))
            addressDB.setNumber(address.getNumber());
        if (!address.getPostalCode().equals(addressDB.getPostalCode()))
            addressDB.setPostalCode(address.getPostalCode());

        return addressRepository.save(addressDB);
    }
}
