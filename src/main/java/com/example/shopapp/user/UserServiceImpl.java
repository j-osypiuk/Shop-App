package com.example.shopapp.user;

import com.example.shopapp.user.dto.RequestUserDto;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user, Role userRole) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRole);
        return userRepository
                .save(user);
    }

    @Override
    public User getUserById(Long id) throws ObjectNotFoundException {
        Optional<User> customerDB = userRepository.findById(id);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with id = " + id + " not found");

        return customerDB.get();
    }

    @Override
    public List<User> getAllUsers() throws ObjectNotFoundException {
        List<User> customersDB = userRepository.findAll();

        if (customersDB.isEmpty()) throw new ObjectNotFoundException("No customers found");

        return customersDB;
    }

    @Override
    public User getUserByEmail(String email) throws ObjectNotFoundException {
        Optional<User> customerDB = userRepository.findUserByEmailIgnoreCase(email);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with email = " + email + " not found");

        return customerDB.get();
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        Optional<User> customerDB = userRepository.findUserByPhoneNumber(phoneNumber);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with phone number = " + phoneNumber + " not found");

        return customerDB.get();
    }

    @Override
    public User updateUserById(RequestUserDto requestUserDto, Long id) throws ObjectNotFoundException {
        Optional<User> customerDB = userRepository.findById(id);

        if (customerDB.isEmpty()) throw new ObjectNotFoundException("Customer with id = " + id + " not found");

        if (!requestUserDto.firstName().equals(customerDB.get().getFirstName()))
            customerDB.get().setFirstName(requestUserDto.firstName());
        if (!requestUserDto.lastName().equals(customerDB.get().getLastName()))
            customerDB.get().setLastName(requestUserDto.lastName());
        if (!requestUserDto.email().equals(customerDB.get().getEmail()))
            customerDB.get().setEmail(requestUserDto.email());
        if (requestUserDto.age() != customerDB.get().getAge())
            customerDB.get().setAge(requestUserDto.age());
        if (!requestUserDto.gender().equals(customerDB.get().getGender()))
            customerDB.get().setGender(requestUserDto.gender());
        if (!requestUserDto.phoneNumber().equals(customerDB.get().getPhoneNumber()))
            customerDB.get().setPhoneNumber(requestUserDto.phoneNumber());

        return userRepository.save(customerDB.get());
    }

    @Override
    public void deleteUserById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = userRepository.deleteUserByUserId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("Customer with id = " + id + " not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return getUserByEmail(username);
        } catch (ObjectNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
