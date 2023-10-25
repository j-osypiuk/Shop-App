package com.example.shopapp.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByCityAndStreetAndNumberAndPostalCode(String city, String street, String number, String postalCode);
}
