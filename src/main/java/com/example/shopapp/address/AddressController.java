package com.example.shopapp.address;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.address.dto.RequestAddressDto;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAddressDto> updateAddressById(@PathVariable("id") Long id, @Valid @RequestBody RequestAddressDto requestAddressDto) throws ObjectNotFoundException {
        Address address = addressService.updateAddressById(
                id,
                AddressDtoMapper.mapRequestAddressDtoToAddress(requestAddressDto)
        );

        return new ResponseEntity<>(
                AddressDtoMapper.mapAddressToResponseAddressDto(address),
                HttpStatus.OK
        );
    }
}
