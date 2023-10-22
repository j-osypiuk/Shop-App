package com.example.shopapp.customer;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByEmailIgnoreCase(String email);
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);
    @Transactional
    Integer deleteCustomerByCustomerId(Long id);
}
