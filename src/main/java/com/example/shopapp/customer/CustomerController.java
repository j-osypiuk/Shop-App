package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public CustomerDto saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable("id") Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(params = "email")
    public CustomerDto getCustomerByEmail(@RequestParam("email") String email) {
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping(params = "phone")
    public CustomerDto getCustomerByPhoneNumber(@RequestParam("phone") String phoneNumber) {
        return customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomerById(@RequestBody Customer customer, @PathVariable("id") Long id) {
        return customerService.updateCustomerById(customer, id);
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerById(@PathVariable("id") Long id) {
        customerService.deleteCustomerById(id);
        return "Customer with id " + id + " deleted successfully";
    }
}
