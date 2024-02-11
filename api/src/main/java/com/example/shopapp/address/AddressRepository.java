package com.example.shopapp.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(
            value = "SELECT * " +
                    "FROM address a " +
                    "WHERE city = :city " +
                    "      AND a.street = :street " +
                    "      AND a.number = :number " +
                    "      AND a.postal_code = :postalCode " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<Address> findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
            @Param("city") String city,
            @Param("street") String street,
            @Param("number") String number,
            @Param("postalCode") String postalCode
    );
}
