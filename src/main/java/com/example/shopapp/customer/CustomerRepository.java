package com.example.shopapp.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByEmailIgnoreCase(String email);
    Customer findCustomerByPhoneNumber(String phoneNumber);
}
