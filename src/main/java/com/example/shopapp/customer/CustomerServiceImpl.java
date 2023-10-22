package com.example.shopapp.customer;

import com.example.shopapp.customer.dto.RequestCustomerDto;
import com.example.shopapp.customer.dto.ResponseCustomerDto;
import com.example.shopapp.customer.dto.CustomerDtoMapper;
import com.example.shopapp.error.exception.ObjectNotFoundException;
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
    public ResponseCustomerDto getCustomerById(Long id) throws ObjectNotFoundException {
        Optional<Customer> customerDB = customerRepository.findById(id);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with id = " + id + " not found");

        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB.get());
    }

    @Override
    public List<ResponseCustomerDto> getAllCustomers() throws ObjectNotFoundException {
        List<Customer> customersDB = customerRepository.findAll();

        if (customersDB.isEmpty()) throw new ObjectNotFoundException("No customers found");

        return CustomerDtoMapper.mapCustomersListToCustomerDtoList(customersDB);
    }

    @Override
    public ResponseCustomerDto getCustomerByEmail(String email) throws ObjectNotFoundException {
        Optional<Customer> customerDB = customerRepository.findCustomerByEmailIgnoreCase(email);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with email = " + email + " not found");

        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB.get());
    }

    @Override
    public ResponseCustomerDto getCustomerByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        Optional<Customer> customerDB = customerRepository.findCustomerByPhoneNumber(phoneNumber);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with phone number = " + phoneNumber + " not found");

        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(customerDB.get());
    }

    @Override
    public ResponseCustomerDto updateCustomerById(RequestCustomerDto requestCustomerDto, Long id) throws ObjectNotFoundException {
        Optional<Customer> customerDB = customerRepository.findById(id);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with id = " + id + " not found");

        if (!requestCustomerDto.firstName().equals(customerDB.get().getFirstName()))
            customerDB.get().setFirstName(requestCustomerDto.firstName());
        if (!requestCustomerDto.lastName().equals(customerDB.get().getLastName()))
            customerDB.get().setLastName(requestCustomerDto.lastName());
        if (!requestCustomerDto.email().equals(customerDB.get().getEmail()))
            customerDB.get().setEmail(requestCustomerDto.email());
        if (requestCustomerDto.age() != customerDB.get().getAge())
            customerDB.get().setAge(requestCustomerDto.age());
        if (!requestCustomerDto.gender().equals(customerDB.get().getGender()))
            customerDB.get().setGender(requestCustomerDto.gender());
        if (!requestCustomerDto.phoneNumber().equals(customerDB.get().getPhoneNumber()))
            customerDB.get().setPhoneNumber(requestCustomerDto.phoneNumber());

        Customer updatedCustomer = customerRepository.save(customerDB.get());
        return CustomerDtoMapper.mapCustomerToResponseCustomerDto(updatedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = customerRepository.deleteCustomerByCustomerId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Customer with id = " + id + " not found");
    }
}
