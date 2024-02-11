package com.example.shopapp.user;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.InvalidPasswordException;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.user.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/customer")
    public ResponseEntity<ResponseUserDto> saveCustomer(@Valid @RequestBody PostUserDto postUser) throws DuplicateUniqueValueException {
        User customer = userService.saveUser(UserDtoMapper.mapPostUserDtoToUser(postUser), Role.CUSTOMER);

        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(customer),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/employee")
    public ResponseEntity<ResponseUserDto> saveEmployee(@Valid @RequestBody PostUserDto postUser) throws DuplicateUniqueValueException {
        User employee = userService.saveUser(UserDtoMapper.mapPostUserDtoToUser(postUser), Role.EMPLOYEE);

        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(employee),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserById(@PathVariable("id") Long id, Principal principal) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserById(id)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getAllUsers() throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserListToUserDtoList(userService.getAllUsers()),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "email")
    public ResponseEntity<ResponseUserDto> getUserByEmail(@RequestParam("email") String email) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserByEmail(email)),
                HttpStatus.OK
        );
    }

    @GetMapping(params = "phone")
    public ResponseEntity<ResponseUserDto> getUserByPhoneNumber(@RequestParam("phone") String phoneNumber) throws ObjectNotFoundException {
        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(userService.getUserByPhoneNumber(phoneNumber)),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUserById(@Valid @RequestBody PutUserDto putUser, @PathVariable("id") Long id) throws ObjectNotFoundException, DuplicateUniqueValueException {
        User user = userService.updateUserById(id, UserDtoMapper.mapPutUserDtoToUser(putUser));

        return new ResponseEntity<>(
                UserDtoMapper.mapUserToResponseUserDto(user),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePasswordByUserId(@PathVariable("id") Long id, @RequestBody PasswordDto newPassword) throws ObjectNotFoundException, InvalidPasswordException {
        userService.updatePasswordByUserId(id, newPassword.password());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
