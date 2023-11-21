package com.example.shopapp.address;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        testAddress = Address.builder()
                .country("Germany")
                .region("Bavaria")
                .city("Munich")
                .street("Langestrasse")
                .number("23")
                .postalCode("21-444")
                .build();
    }

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(addressRepository).isNotNull();
    }

    @Test
    void findByCityAndStreetAndNumberAndPostalCodeReturnsProperAddress() {
        addressRepository.save(testAddress);

        Optional<Address> addressDB = addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                testAddress.getCity(),
                testAddress.getStreet(),
                testAddress.getNumber(),
                testAddress.getPostalCode()
        );

        assertDoesNotThrow(addressDB::get);
        assertEquals(addressDB.get().getCity(), testAddress.getCity());
        assertEquals(addressDB.get().getStreet(), testAddress.getStreet());
        assertEquals(addressDB.get().getNumber(), testAddress.getNumber());
        assertEquals(addressDB.get().getPostalCode(), testAddress.getPostalCode());
    }

    @Test
    void findByCityAndStreetAndNumberAndPostalCodeThrowsNoSuchElementException() {
        addressRepository.save(testAddress);

        Optional<Address> addressDB = addressRepository.findDistinctAddressByCityAndStreetAndNumberAndPostalCode(
                "OtherCity",
                testAddress.getStreet(),
                testAddress.getNumber(),
                testAddress.getPostalCode()
        );

        assertThrows(NoSuchElementException.class, addressDB::get);
    }
}