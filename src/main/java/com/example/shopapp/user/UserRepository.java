package com.example.shopapp.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);
    @Query(value = "SELECT u FROM User u WHERE u.phoneNumber = ?1")
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM User u WHERE u.userId = ?1")
    Integer deleteUserById(Long id);
    boolean existsUserByEmail(String email);
    boolean existsUserByPhoneNumber(String phoneNumber);
}
