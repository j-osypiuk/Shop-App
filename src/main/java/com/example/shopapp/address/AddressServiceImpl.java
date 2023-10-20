package com.example.shopapp.address;

import com.example.shopapp.address.dto.AddressDto;
import com.example.shopapp.address.dto.AddressDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    public AddressDto updateAddressById(Long id, Address address) {
        Address addressDB = addressRepository.findById(id).get();

        if (address.getCountry() != null && !address.getCountry().isEmpty()) addressDB.setCountry(address.getCountry());
        if (address.getRegion() != null && !address.getRegion().isEmpty()) addressDB.setRegion(address.getRegion());
        if (address.getCity() != null && !address.getCity().isEmpty()) addressDB.setCity(address.getCity());
        if (address.getStreet() != null && !address.getStreet().isEmpty()) addressDB.setStreet(address.getStreet());
        if (address.getNumber() != null && !address.getNumber().isEmpty()) addressDB.setNumber(address.getNumber());
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) addressDB.setPostalCode(address.getPostalCode());

        Address updatedAddress = addressRepository.save(addressDB);
        return AddressDtoMapper.mapAddressToAddressDto(updatedAddress);
    }
}
