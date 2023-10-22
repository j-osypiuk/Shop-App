package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;
import com.example.shopapp.customer.dto.CustomerDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseCustomerDto saveCustomer(RequestCustomerDto requestCustomerDto) {
        Customer customerDB = customerRepository
                .save(CustomerDtoMapper.mapRequestCustomerDtoToCustomer(requestCustomerDto));
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB);
    }

    @Override
    public ResponseCustomerDto getCustomerById(Long id) {
        Customer customerDB = customerRepository.findById(id).get();
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB);
    }

    @Override
    public List<ResponseCustomerDto> getAllCustomers() {
        List<Customer> customersDB = customerRepository.findAll();
        return CustomerDtoMapper.mapCustomersListToCustomerDtoList(customersDB);
    }

    @Override
    public ResponseCustomerDto getCustomerByEmail(String email) {
        Customer customerDB = customerRepository.findCustomerByEmailIgnoreCase(email);
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB);
    }

    @Override
    public ResponseCustomerDto getCustomerByPhoneNumber(String phoneNumber) {
        Customer customerDB = customerRepository.findCustomerByPhoneNumber(phoneNumber);
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB);
    }

    @Override
    public ResponseCustomerDto updateCustomerById(RequestCustomerDto requestCustomerDto, Long id) {
        Optional<Customer> customerDB = customerRepository.findById(id);

        if (!requestCustomerDto.firstName().equals(customerDB.get().getFirstName())) customerDB.get().setFirstName(requestCustomerDto.firstName());
        if (!requestCustomerDto.lastName().equals(customerDB.get().getLastName())) customerDB.get().setLastName(requestCustomerDto.lastName());
        if (!requestCustomerDto.email().equals(customerDB.get().getEmail())) customerDB.get().setEmail(requestCustomerDto.email());
        if (requestCustomerDto.age() != customerDB.get().getAge()) customerDB.get().setAge(requestCustomerDto.age());
        if (!requestCustomerDto.gender().equals(customerDB.get().getGender())) customerDB.get().setGender(requestCustomerDto.gender());
        if (!requestCustomerDto.phoneNumber().equals(customerDB.get().getPhoneNumber())) customerDB.get().setPhoneNumber(requestCustomerDto.phoneNumber());

        Customer updatedCustomer = customerRepository.save(customerDB.get());
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(updatedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
