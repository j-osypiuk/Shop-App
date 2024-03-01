package com.shopapp.user;

import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.InvalidPasswordException;
import com.shopapp.exception.ObjectNotFoundException;
import com.shopapp.user.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/customer")
    public ResponseEntity<Map<String, Long>> saveCustomer(@Valid @RequestBody RequestUserDto postUser) throws DuplicateUniqueValueException {
        User customer = userService.saveUser(UserDtoMapper.mapRequestUserDtoToUser(postUser), Role.ROLE_CUSTOMER);

        return new ResponseEntity<>(
                Map.of("userId", customer.getUserId()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/employee")
    public ResponseEntity<Map<String, Long>> saveEmployee(@Valid @RequestBody RequestUserDto postUser) throws DuplicateUniqueValueException {
        User employee = userService.saveUser(UserDtoMapper.mapRequestUserDtoToUser(postUser), Role.ROLE_EMPLOYEE);

        return new ResponseEntity<>(
                Map.of("userId", employee.getUserId()),
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
    public ResponseEntity<Map<String, Long>> updateUserById(@Valid @RequestBody RequestUserDto putUser, @PathVariable("id") Long id) throws ObjectNotFoundException, DuplicateUniqueValueException {
        User user = userService.updateUserById(id, UserDtoMapper.mapRequestUserDtoToUser(putUser));

        return new ResponseEntity<>(
                Map.of("userId", user.getUserId()),
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
