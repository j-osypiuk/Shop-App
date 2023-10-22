package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;

import java.util.List;

public interface CustomerService {
    ResponseCustomerDto saveCustomer(RequestCustomerDto requestCustomerDto);
    ResponseCustomerDto getCustomerById(Long id);
    List<ResponseCustomerDto> getAllCustomers();
    ResponseCustomerDto getCustomerByEmail(String email);
    ResponseCustomerDto getCustomerByPhoneNumber(String phoneNumber);
    ResponseCustomerDto updateCustomerById(RequestCustomerDto requestCustomerDto, Long id);
    void deleteCustomerById(Long id);
}
