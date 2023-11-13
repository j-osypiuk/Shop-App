package com.example.shopapp.user;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.InvalidPasswordException;
import com.example.shopapp.exception.ObjectNotFoundException;

import java.util.List;

public interface UserService {
    User saveUser(User user, Role userRole) throws DuplicateUniqueValueException;
    User getUserById(Long id) throws ObjectNotFoundException;
    List<User> getAllUsers() throws ObjectNotFoundException;
    User getUserByEmail(String email) throws ObjectNotFoundException;
    User getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException;
    User updateUserById(Long id, User user) throws ObjectNotFoundException, DuplicateUniqueValueException;
    void updatePasswordByUserId(Long id, String newPassword) throws InvalidPasswordException, ObjectNotFoundException;
    void deleteUserById(Long id) throws ObjectNotFoundException;
}
