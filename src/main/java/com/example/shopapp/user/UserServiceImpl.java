package com.example.shopapp.user;

import com.example.shopapp.exception.InvalidPasswordException;
import com.example.shopapp.exception.ObjectNotFoundException;
import com.example.shopapp.user.dto.PostUserDto;
import com.example.shopapp.user.dto.PutUserDto;
import com.example.shopapp.user.dto.ResponseUserDto;
import com.example.shopapp.user.dto.UserDtoMapper;
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
    public ResponseUserDto saveUser(PostUserDto postUserDto, Role userRole) {
        User user = UserDtoMapper.mapPostUserDtoToUser(postUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);
        return UserDtoMapper.mapUserToResponseUserDto(user);
    }

    @Override
    public ResponseUserDto getUserById(Long id) throws ObjectNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        return UserDtoMapper.mapUserToResponseUserDto(userDB);
    }

    @Override
    public List<ResponseUserDto> getAllUsers() throws ObjectNotFoundException {
        List<User> usersDB = userRepository.findAll();

        if (usersDB.isEmpty()) throw new ObjectNotFoundException("No users found");

        return UserDtoMapper.mapUserListToUserDtoList(usersDB);
    }

    @Override
    public ResponseUserDto getUserByEmail(String email) throws ObjectNotFoundException {
        User userDB = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email = " + email + " not found"));

        return UserDtoMapper.mapUserToResponseUserDto(userDB);
    }

    @Override
    public ResponseUserDto getUserByPhoneNumber(String phoneNumber) throws ObjectNotFoundException {
        User userDB = userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ObjectNotFoundException("User with phone number = " + phoneNumber + " not found"));

        return UserDtoMapper.mapUserToResponseUserDto(userDB);
    }

    @Override
    public ResponseUserDto updateUserById(PutUserDto putUserDto, Long id) throws ObjectNotFoundException {
        User userDB = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id = " + id + " not found"));

        if (!putUserDto.firstName().equals(userDB.getFirstName()))
            userDB.setFirstName(putUserDto.firstName());
        if (!putUserDto.lastName().equals(userDB.getLastName()))
            userDB.setLastName(putUserDto.lastName());
        if (putUserDto.birthDate() != userDB.getBirthDate())
            userDB.setBirthDate(putUserDto.birthDate());
        if (!putUserDto.gender().equals(userDB.getGender()))
            userDB.setGender(putUserDto.gender());
        if (!putUserDto.phoneNumber().equals(userDB.getPhoneNumber()))
            userDB.setPhoneNumber(putUserDto.phoneNumber());

        userRepository.save(userDB);
        return UserDtoMapper.mapUserToResponseUserDto(userDB);
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
            return userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("User with email = " + username + " not found"));
        } catch (ObjectNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
