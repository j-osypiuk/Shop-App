package com.example.shopapp.user;

import com.example.shopapp.exception.InvalidPasswordException;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.user.dto.PostUserDto;
import com.example.shopapp.user.dto.PutUserDto;
import com.example.shopapp.user.dto.ResponseUserDto;

import java.util.List;

public interface UserService {
    ResponseUserDto saveUser(PostUserDto user, Role userRole);
    ResponseUserDto getUserById(Long id) throws ObjectNotFoundException;
    List<ResponseUserDto> getAllUsers() throws ObjectNotFoundException;
    ResponseUserDto getUserByEmail(String email) throws ObjectNotFoundException;
    ResponseUserDto getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException;
    ResponseUserDto updateUserById(PutUserDto user, Long id) throws ObjectNotFoundException;
    void updatePasswordByUserId(Long id, String newPassword) throws InvalidPasswordException, ObjectNotFoundException;
    void deleteUserById(Long id) throws ObjectNotFoundException;
}
