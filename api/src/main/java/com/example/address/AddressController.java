package com.example.address;

import com.example.address.dto.AddressDtoMapper;
import com.example.address.dto.RequestAddressDto;
import com.example.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Long>> updateAddressById(@PathVariable("id") Long id, @Valid @RequestBody RequestAddressDto requestAddressDto) throws ObjectNotFoundException {
        Address address = addressService.updateAddressById(
                id,
                AddressDtoMapper.mapRequestAddressDtoToAddress(requestAddressDto)
        );

        return new ResponseEntity<>(
                Map.of("addressId", address.getAddressId()),
                HttpStatus.OK
        );
    }
}
