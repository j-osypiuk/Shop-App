package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseCustomerDto> saveCustomer(@Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        return new ResponseEntity<>(
                customerService.saveCustomer(requestCustomerDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCustomerDto> getCustomerById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                customerService.getCustomerById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseCustomerDto>> getAllCustomers() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                customerService.getAllCustomers(),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "email")
    public ResponseEntity<ResponseCustomerDto> getCustomerByEmail(@RequestParam("email") String email) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                customerService.getCustomerByEmail(email),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "phone")
    public ResponseEntity<ResponseCustomerDto> getCustomerByPhoneNumber(@RequestParam("phone") String phoneNumber) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                customerService.getCustomerByPhoneNumber(phoneNumber),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCustomerDto> updateCustomerById(@Valid @RequestBody RequestCustomerDto requestCustomerDto, @PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                customerService.updateCustomerById(requestCustomerDto, id),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
