package com.example.shopapp.customer.dto;

import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.customer.Customer;

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
                new ResponseAddressDto(
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

    public static OrderCustomerDto mapCustomerToOrderCustomerDto(Customer customer) {
        return new OrderCustomerDto(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }

    public static List<CustomerDto> mapCustomersListToCustomerDtoList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerDtoMapper::mapCustomerToCustomerDto).collect(Collectors.toList());
    }
}
