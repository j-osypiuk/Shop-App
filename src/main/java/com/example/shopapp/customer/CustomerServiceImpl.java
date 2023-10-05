package com.example.shopapp.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto saveCustomer(Customer customer) {
        Customer customerDB = customerRepository.save(customer);
        return CustomerDtoMapper.mapCustomerToCustomerDto(customerDB);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customerDB = customerRepository.findById(id).get();
        return CustomerDtoMapper.mapCustomerToCustomerDto(customerDB);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customersDB = customerRepository.findAll();
        return CustomerDtoMapper.mapCustomersListToCustomerDtoList(customersDB);
    }

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        Customer customerDB = customerRepository.findCustomerByEmailIgnoreCase(email);
        return CustomerDtoMapper.mapCustomerToCustomerDto(customerDB);
    }

    @Override
    public CustomerDto getCustomerByPhoneNumber(String phoneNumber) {
        Customer customerDB = customerRepository.findCustomerByPhoneNumber(phoneNumber);
        return CustomerDtoMapper.mapCustomerToCustomerDto(customerDB);
    }

    @Override
    public CustomerDto updateCustomerById(Customer customer, Long id) {
        Customer customerDB = customerRepository.findById(id).get();

        if (customer.getFirstName() != null && !customer.getFirstName().isEmpty()) customerDB.setFirstName(customer.getFirstName());
        if (customer.getLastName() != null && !customer.getLastName().isEmpty()) customerDB.setLastName(customer.getLastName());
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) customerDB.setEmail(customer.getEmail());
        if (customer.getAge() != customerDB.getAge() && customer.getAge() > 0) customerDB.setAge(customer.getAge());
        if (customer.getGender() != null) customerDB.setGender(customer.getGender());
        if (customer.getPhoneNumber() != null && !customer.getPhoneNumber().isEmpty()) customerDB.setPhoneNumber(customer.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(customerDB);
        return CustomerDtoMapper.mapCustomerToCustomerDto(updatedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
