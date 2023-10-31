package com.example.shopapp.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailIgnoreCase(String email);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    @Transactional
    Integer deleteUserByUserId(Long id);
}
