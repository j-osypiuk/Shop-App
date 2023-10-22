package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;

import java.util.List;

public interface CustomerService {
    ResponseCustomerDto saveCustomer(RequestCustomerDto requestCustomerDto);
    ResponseCustomerDto getCustomerById(Long id) throws ObjectNotFoundException;
    List<ResponseCustomerDto> getAllCustomers() throws ObjectNotFoundException;
    ResponseCustomerDto getCustomerByEmail(String email) throws ObjectNotFoundException;
    ResponseCustomerDto getCustomerByPhoneNumber(String phoneNumber) throws ObjectNotFoundException;
    ResponseCustomerDto updateCustomerById(RequestCustomerDto requestCustomerDto, Long id) throws ObjectNotFoundException;
    void deleteCustomerById(Long id) throws ObjectNotFoundException;
}
