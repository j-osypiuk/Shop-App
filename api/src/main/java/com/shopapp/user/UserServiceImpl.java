package com.shopapp.user;

import com.shopapp.exception.DuplicateUniqueValueException;
import com.shopapp.exception.InvalidPasswordException;
import com.shopapp.exception.ObjectNotFoundException;
import org.springframework.context.annotation.Lazy;
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

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user, Role userRole) throws DuplicateUniqueValueException {
        if (userRepository.existsUserByEmail(user.getEmail()))
            throw new DuplicateUniqueValueException("User with email = " + user.getEmail() + " already exists");
        if (userRepository.existsUserByPhoneNumber(user.getPhoneNumber()))
            throw new DuplicateUniqueValueException("User with phone = " + user.getPhoneNumber() + " already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRole);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws ObjectNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() throws ObjectNotFoundException {
        List<User> usersDB = userRepository.findAll();

        if (usersDB.isEmpty()) throw new ObjectNotFoundException("No users found");

        return usersDB;
    }

    @Override
    public User getUserByEmail(String email) throws ObjectNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email = " + email + " not found"));
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        return userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ObjectNotFoundException("User with phone number = " + phoneNumber + " not found"));
    }

    @Override
    public User updateUserById(Long id, User user) throws ObjectNotFoundException, DuplicateUniqueValueException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        if (userRepository.existsUserByPhoneNumber(user.getPhoneNumber()))
            throw new DuplicateUniqueValueException("User with phone = " + user.getPhoneNumber() + " already exists");

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
        Integer isDeleted = userRepository.deleteUserById(id);

        if (isDeleted == 0) throw new ObjectNotFoundException("User with id = " + id + " not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            return userRepository.findUserByEmail(username)
                    .orElseThrow(() -> new ObjectNotFoundException("User with email = " + username + " not found"));
        } catch (ObjectNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
