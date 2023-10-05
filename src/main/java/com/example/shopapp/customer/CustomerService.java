package com.example.shopapp.customer;

import java.util.List;

public interface CustomerService {
    CustomerDto saveCustomer(Customer customer);
    CustomerDto getCustomerById(Long id);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerByEmail(String email);
    CustomerDto getCustomerByPhoneNumber(String phoneNumber);
    CustomerDto updateCustomerById(Customer customer, Long id);
    void deleteCustomerById(Long id);
}
