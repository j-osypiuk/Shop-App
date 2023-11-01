package com.example.shopapp.user;

import com.example.shopapp.error.exception.InvalidPasswordException;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import com.example.shopapp.user.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/customer")
    public ResponseEntity<ResponseUserDto> saveCustomer(@Valid @RequestBody PostUserDto postUser) {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(
                        userService.saveUser(UserDtoMapper.mapPostUserDtoToUser(postUser), Role.CUSTOMER)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseUserDto> saveEmployee(@Valid @RequestBody PostUserDto postUser) {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(
                        userService.saveUser(UserDtoMapper.mapPostUserDtoToUser(postUser), Role.EMPLOYEE)),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserListToUserDtoList(userService.getAllUsers()),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "email")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseUserDto> getUserByEmail(@RequestParam("email") String email) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserByEmail(email)),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "phone")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<ResponseUserDto> getUserByPhoneNumber(@RequestParam("phone") String phoneNumber) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserByPhoneNumber(phoneNumber)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<ResponseUserDto> updateUserById(@Valid @RequestBody PutUserDto putUser, @PathVariable("id") Long id) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(
                        userService.updateUserById(UserDtoMapper.mapPutUserDtoToUser(putUser), id)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<Void> updatePasswordByUserId(@PathVariable("id") Long id, @RequestBody PasswordDto newPassword) throws ObjectNotFoundException, InvalidPasswordException {
        userService.updatePasswordByUserId(id, newPassword.password());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
