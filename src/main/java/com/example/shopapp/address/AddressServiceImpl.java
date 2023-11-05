package com.example.shopapp.address;

import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public ResponseAddressDto updateAddressById(Long id, RequestAddressDto requestAddressDto) throws ObjectNotFoundException {
        Optional<Address> addressDB = addressRepository.findById(id);

        if (addressDB.isEmpty()) throw new ObjectNotFoundException("Address with id = " + id + " not found");

        if (!requestAddressDto.country().equals(addressDB.get().getCountry())) addressDB.get().setCountry(requestAddressDto.country());
        if (!requestAddressDto.region().equals(addressDB.get().getRegion())) addressDB.get().setRegion(requestAddressDto.region());
        if (!requestAddressDto.city().equals(addressDB.get().getCity())) addressDB.get().setCity(requestAddressDto.city());
        if (!requestAddressDto.street().equals(addressDB.get().getStreet())) addressDB.get().setStreet(requestAddressDto.street());
        if (!requestAddressDto.number().equals(addressDB.get().getNumber())) addressDB.get().setNumber(requestAddressDto.number());
        if (!requestAddressDto.postalCode().equals(addressDB.get().getPostalCode())) addressDB.get().setPostalCode(requestAddressDto.postalCode());

        Address updatedAddress = addressRepository.save(addressDB.get());
        return AddressDtoMapper.mapAddressToResponseAddressDto(updatedAddress);
    }
}
