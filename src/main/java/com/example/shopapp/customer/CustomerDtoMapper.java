package com.example.shopapp.customer;

import com.example.shopapp.address.AddressDto;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerDtoMapper {

    public static CustomerDto mapCustomerToCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender(),
                customer.getPhoneNumber(),
                new AddressDto(
                        customer.getAddress().getAddressId(),
                        customer.getAddress().getCountry(),
                        customer.getAddress().getRegion(),
                        customer.getAddress().getCity(),
                        customer.getAddress().getStreet(),
                        customer.getAddress().getNumber(),
                        customer.getAddress().getPostalCode()
                )
        );
    }

    public static List<CustomerDto> mapCustomersListToCustomerDtoList(List<Customer> customers) {
        return customers.stream()
                .map(customer -> new CustomerDto(
                        customer.getCustomerId(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getAge(),
                        customer.getGender(),
                        customer.getPhoneNumber(),
                        new AddressDto(
                                customer.getAddress().getAddressId(),
                                customer.getAddress().getCountry(),
                                customer.getAddress().getRegion(),
                                customer.getAddress().getCity(),
                                customer.getAddress().getStreet(),
                                customer.getAddress().getNumber(),
                                customer.getAddress().getPostalCode()
                        )
                )).collect(Collectors.toList());
    }
}
