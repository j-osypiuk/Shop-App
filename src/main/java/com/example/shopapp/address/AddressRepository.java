package com.example.shopapp.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "SELECT a FROM Address a WHERE a.city = ?1 AND a.street = ?2 AND a.number = ?3 AND a.postalCode = ?4")
    Optional<Address> findAddressByCityAndStreetAndNumberAndPostalCode(String city, String street, String number, String postalCode);
}
