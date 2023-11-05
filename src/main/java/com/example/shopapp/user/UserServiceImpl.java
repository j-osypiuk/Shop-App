package com.example.shopapp.user;

import com.example.shopapp.error.exception.InvalidPasswordException;
import com.example.shopapp.error.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return userRepository
                .save(user);
    }

    @Override
    public User getUserById(Long id) throws ObjectNotFoundException {
        Optional<User> userDB = userRepository.findById(id);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with id = " + id + " not found");

        return userDB.get();
    }

    @Override
    public List<User> getAllUsers() throws ObjectNotFoundException {
        List<User> usersDB = userRepository.findAll();

        if (usersDB.isEmpty())
            throw new ObjectNotFoundException("No users found");

        return usersDB;
    }

    @Override
    public User getUserByEmail(String email) throws ObjectNotFoundException {
        Optional<User> userDB = userRepository.findUserByEmailIgnoreCase(email);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with email = " + email + " not found");

        return userDB.get();
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        Optional<User> userDB = userRepository.findUserByPhoneNumber(phoneNumber);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with phone number = " + phoneNumber + " not found");

        return userDB.get();
    }

    @Override
    public User updateUserById(User user, Long id) throws ObjectNotFoundException {
        Optional<User> userDB = userRepository.findById(id);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with id = " + id + " not found");

        if (!user.getFirstName().equals(userDB.get().getFirstName()))
            userDB.get().setFirstName(user.getFirstName());
        if (!user.getLastName().equals(userDB.get().getLastName()))
            userDB.get().setLastName(user.getLastName());
        if (user.getBirthDate() != userDB.get().getBirthDate())
            userDB.get().setBirthDate(user.getBirthDate());
        if (!user.getGender().equals(userDB.get().getGender()))
            userDB.get().setGender(user.getGender());
        if (!user.getPhoneNumber().equals(userDB.get().getPhoneNumber()))
            userDB.get().setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(userDB.get());
    }

    @Override
    public void updatePasswordByUserId(Long id, String newPassword) throws InvalidPasswordException, ObjectNotFoundException {
        Optional<User> userDB = userRepository.findById(id);

        if (userDB.isEmpty())
            throw new ObjectNotFoundException("User with id = " + id + " not found");
        if (newPassword.isEmpty())
            throw new InvalidPasswordException("User password cannot be blank");
        if (newPassword.length() > 100)
            throw new InvalidPasswordException("User password cannot contain more than 100 characters");

        userDB.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userDB.get());
    }

    @Override
    public void deleteUserById(Long id) throws ObjectNotFoundException {
        Integer isDeleted = userRepository.deleteUserByUserId(id);

        if (isDeleted == 0)
            throw new ObjectNotFoundException("User with id = " + id + " not found");
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
