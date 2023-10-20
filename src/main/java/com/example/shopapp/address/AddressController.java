package com.example.shopapp.address;

import com.example.shopapp.address.dto.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PutMapping("/{id}")
    public AddressDto updateAddressById(@PathVariable("id") Long id, @RequestBody Address address) {
        return addressService.updateAddressById(id, address);
    }
}
