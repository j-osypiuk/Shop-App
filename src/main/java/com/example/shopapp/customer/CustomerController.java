package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseCustomerDto saveCustomer(@Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        return customerService.saveCustomer(requestCustomerDto);
    }

    @GetMapping("/{id}")
    public ResponseCustomerDto getCustomerById(@PathVariable("id") Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public List<ResponseCustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(params = "email")
    public ResponseCustomerDto getCustomerByEmail(@RequestParam("email") String email) {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping(params = "phone")
    public ResponseCustomerDto getCustomerByPhoneNumber(@RequestParam("phone") String phoneNumber) {
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    @PutMapping("/{id}")
    public ResponseCustomerDto updateCustomerById(@Valid @RequestBody RequestCustomerDto requestCustomerDto, @PathVariable("id") Long id) {
        return customerService.updateCustomerById(requestCustomerDto, id);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerById(@PathVariable("id") Long id) {
        customerService.deleteCustomerById(id);
        return "Customer with id " + id + " deleted successfully";
    }
}
