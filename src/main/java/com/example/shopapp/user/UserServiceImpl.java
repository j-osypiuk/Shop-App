package com.example.shopapp.user;

import com.example.shopapp.error.exception.InvalidPasswordException;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user, Role userRole) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws ObjectNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        return userDB;
    }

    @Override
    public List<User> getAllUsers() throws ObjectNotFoundException {
        List<User> usersDB = userRepository.findAll();

        if (usersDB.isEmpty()) throw new ObjectNotFoundException("No users found");

        return usersDB;
    }

    @Override
    public User getUserByEmail(String email) throws ObjectNotFoundException {
        User userDB = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email = " + email + " not found"));

        return userDB;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        User userDB = userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ObjectNotFoundException("User with phone number = " + phoneNumber + " not found"));

        return userDB;
    }

    @Override
    public User updateUserById(User user, Long id) throws ObjectNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        if (!user.getFirstName().equals(userDB.getFirstName()))
            userDB.setFirstName(user.getFirstName());
        if (!user.getLastName().equals(userDB.getLastName()))
            userDB.setLastName(user.getLastName());
        if (user.getBirthDate() != userDB.getBirthDate())
            userDB.setBirthDate(user.getBirthDate());
        if (!user.getGender().equals(userDB.getGender()))
            userDB.setGender(user.getGender());
        if (!user.getPhoneNumber().equals(userDB.getPhoneNumber()))
            userDB.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(userDB);
    }

    @Override
    public void updatePasswordByUserId(Long id, String newPassword) throws InvalidPasswordException, ObjectNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        if (newPassword.isEmpty())
            throw new InvalidPasswordException("User password cannot be blank");
        if (newPassword.length() > 100)
            throw new InvalidPasswordException("User password cannot contain more than 100 characters");

        userDB.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userDB);
    }

    @Override
    public void deleteUserById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = userRepository.deleteUserByUserId(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("User with id = " + id + " not found");
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
