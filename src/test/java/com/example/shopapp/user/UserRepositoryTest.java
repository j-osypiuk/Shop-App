package com.example.shopapp.user;

import com.example.shopapp.address.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .firstName("Steven")
                .lastName("Smith")
                .email("steven@mail.com")
                .password("password")
                .birthDate(LocalDateTime.now())
                .gender(Gender.MALE)
                .phoneNumber("111222333")
                .role(Role.ADMIN)
                .address(Address.builder()
                        .country("Germany")
                        .region("Bavaria")
                        .city("Munich")
                        .street("Shlasse")
                        .number("23")
                        .postalCode("21-444")
                        .build())
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(userRepository).isNotNull();
    }

    @Test
    void findUserByEmailReturnsProperUser() {
        userRepository.save(testUser);

        Optional<User> userDB = userRepository.findUserByEmail(testUser.getEmail());

        assertDoesNotThrow(userDB::get);
        assertEquals(userDB.get().getEmail(), testUser.getEmail());
    }

    @Test
    void findUserByEmailThrowsNoSuchElementException() {
        userRepository.save(testUser);

        Optional<User> userDB = userRepository.findUserByEmail("other@mail.com");

        assertThrows(NoSuchElementException.class, userDB::get);
    }

    @Test
    void findUserByPhoneNumberReturnsProperUser() {
        userRepository.save(testUser);

        Optional<User> userDB = userRepository.findUserByPhoneNumber(testUser.getPhoneNumber());

        assertDoesNotThrow(userDB::get);
        assertEquals(userDB.get().getPhoneNumber(), testUser.getPhoneNumber());
    }

    @Test
    void findUserByPhoneNumberThrowsNoSuchElementException() {
        userRepository.save(testUser);

        Optional<User> userDB = userRepository.findUserByPhoneNumber("999999999");

        assertThrows(NoSuchElementException.class, userDB::get);
    }

    @Test
    void deleteUserByUserIdDeletesProperUser() {
        userRepository.save(testUser);

        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 1);

        Integer isDeleted = userRepository.deleteUserById(testUser.getUserId());
        assertEquals(isDeleted, 1);

        users = userRepository.findAll();
        assertEquals(users.size(), 0);
    }

    @Test
    void deleteUserByUserIdDoesNotDeleteAnythingIfUserDoesNotExist() {
        userRepository.save(testUser);

        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 1);

        Integer isDeleted = userRepository.deleteUserById(999L);
        assertEquals(isDeleted, 0);

        users = userRepository.findAll();
        assertEquals(users.size(), 1);
    }
}