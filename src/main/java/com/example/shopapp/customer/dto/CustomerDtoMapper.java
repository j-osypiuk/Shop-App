package com.example.shopapp.customer.dto;

import com.example.shopapp.address.dto.AddressDtoMapper;
import com.example.shopapp.address.dto.ResponseAddressDto;
import com.example.shopapp.customer.Customer;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerDtoMapper {

    public static ResponseCustomerDto mapCustomerToResponseCustomerDto(Customer customer) {
        return new ResponseCustomerDto(
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

    public static Customer mapRequestCustomerDtoToCustomer(RequestCustomerDto requestCustomerDto) {
        return Customer.builder()
                .firstName(requestCustomerDto.firstName())
                .lastName(requestCustomerDto.lastName())
                .email(requestCustomerDto.email())
                .age(requestCustomerDto.age())
                .gender(requestCustomerDto.gender())
                .phoneNumber(requestCustomerDto.phoneNumber())
                .address(AddressDtoMapper.mapRequestAddressDtoToAddress(requestCustomerDto.address()))
                .build();
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

    public static List<ResponseCustomerDto> mapCustomersListToCustomerDtoList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerDtoMapper::mapCustomerToResponseCustomerDto).collect(Collectors.toList());
    }
}
