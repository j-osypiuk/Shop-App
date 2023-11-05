package com.example.shopapp.address;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public ResponseAddressDto updateAddressById(Long id, RequestAddressDto requestAddressDto) throws ObjectNotFoundException {
        Address addressDB = addressRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Address with id = " + id + " not found"));

        if (!requestAddressDto.country().equals(addressDB.getCountry()))
            addressDB.setCountry(requestAddressDto.country());
        if (!requestAddressDto.region().equals(addressDB.getRegion()))
            addressDB.setRegion(requestAddressDto.region());
        if (!requestAddressDto.city().equals(addressDB.getCity()))
            addressDB.setCity(requestAddressDto.city());
        if (!requestAddressDto.street().equals(addressDB.getStreet()))
            addressDB.setStreet(requestAddressDto.street());
        if (!requestAddressDto.number().equals(addressDB.getNumber()))
            addressDB.setNumber(requestAddressDto.number());
        if (!requestAddressDto.postalCode().equals(addressDB.getPostalCode()))
            addressDB.setPostalCode(requestAddressDto.postalCode());

        addressRepository.save(addressDB);
        return AddressDtoMapper.mapAddressToResponseAddressDto(addressDB);
    }
}
