package com.example.shopapp.user;

import com.example.shopapp.exception.DuplicateUniqueValueException;
import com.example.shopapp.exception.InvalidPasswordException;
import com.example.shopapp.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void saveUserSavesUser() throws DuplicateUniqueValueException {
        // given
        Role role = Role.EMPLOYEE;
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(false);
        given(userRepository.existsUserByPhoneNumber(user.getPhoneNumber())).willReturn(false);
        given(userRepository.save(user)).willReturn(user);
        // when
        User savedUser = userService.saveUser(user, role);
        // then
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userPasswordCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).existsUserByEmail(userEmailCaptor.capture());
        verify(userRepository).existsUserByPhoneNumber(userPhoneCaptor.capture());
        verify(passwordEncoder).encode(userPasswordCaptor.capture());
        verify(userRepository).save(userCaptor.capture());
        assertThat(userEmailCaptor.getValue()).isEqualTo(user.getEmail());
        assertThat(userPhoneCaptor.getValue()).isEqualTo(user.getPhoneNumber());
        assertThat(userPasswordCaptor.getValue()).isEqualTo(user.getPassword());
        assertThat(userCaptor.getValue()).isEqualTo(user);
        assertEquals(savedUser, user);
    }

    @Test
    void saveUserThrowsExceptionIfOtherExistingUserHasTheSameEmail() {
        // given
        Role role = Role.EMPLOYEE;
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(true);
        // when
        // then
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.saveUser(user, role))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("User with email = " + user.getEmail() + " already exists");
        verify(userRepository).existsUserByEmail(userEmailCaptor.capture());
        verify(userRepository, never()).existsUserByPhoneNumber(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        assertThat(userEmailCaptor.getValue()).isEqualTo(user.getEmail());
    }

    @Test
    void saveUserThrowsExceptionIfOtherExistingUserHasTheSamePhoneNumber() {
        // given
        Role role = Role.EMPLOYEE;
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(false);
        given(userRepository.existsUserByPhoneNumber(user.getPhoneNumber())).willReturn(true);
        // when
        // then
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.saveUser(user, role))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("User with phone = " + user.getPhoneNumber() + " already exists");
        verify(userRepository).existsUserByEmail(userEmailCaptor.capture());
        verify(userRepository).existsUserByPhoneNumber(userPhoneCaptor.capture());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        assertThat(userEmailCaptor.getValue()).isEqualTo(user.getEmail());
        assertThat(userPhoneCaptor.getValue()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    void getUserByIdReturnsUser() throws ObjectNotFoundException {
        // given
        Long userId = 1L;
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        // when
        User foundUser = userService.getUserById(userId);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertEquals(foundUser, user);
    }

    @Test
    void getUserByIdThrowsExceptionIfUserWithGivenIdDoesNotExists() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with id = " + userId + " not found");
        verify(userRepository).findById(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }
    
    @Test
    void getAllUsersReturnsListOfUsers() throws ObjectNotFoundException {
        // given
        User user1 = User.builder().firstName("Adam").build();
        User user2 = User.builder().firstName("Marry").build();
        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));
        // when
        List<User> users = userService.getAllUsers();
        // then
        verify(userRepository).findAll();
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), user1);
        assertEquals(users.get(1), user2);
    }

    @Test
    void getAllUsersThrowsExceptionIfNoUserExists() {
        // given
        given(userRepository.findAll()).willReturn(Collections.emptyList());
        // when
        // then
        assertThatThrownBy(() -> userService.getAllUsers())
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No users found");
        verify(userRepository).findAll();
    }

    @Test
    void getUserByEmailReturnsUser() throws ObjectNotFoundException {
        // given
        String userEmail = "stev@mail.com";
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findUserByEmail(userEmail)).willReturn(Optional.of(user));
        // when
        User foundUser = userService.getUserByEmail(userEmail);
        // then
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findUserByEmail(userEmailCaptor.capture());
        assertThat(userEmailCaptor.getValue()).isEqualTo(userEmail);
        assertEquals(foundUser, user);
    }

    @Test
    void getUserByEmailThrowsExceptionIfUserWithGivenEmailDoesNotExists() {
        // given
        String userEmail = "stev@mail.com";
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findUserByEmail(userEmail)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<String> userEmailCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.getUserByEmail(userEmail))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with email = " + userEmail + " not found");
        verify(userRepository).findUserByEmail(userEmailCaptor.capture());
        assertThat(userEmailCaptor.getValue()).isEqualTo(userEmail);
    }

    @Test
    void getUserByPhoneNumberReturnsUser() throws ObjectNotFoundException {
        // given
        String userPhone = "345543345";
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findUserByPhoneNumber(userPhone)).willReturn(Optional.of(user));
        // when
        User foundUser = userService.getUserByPhoneNumber(userPhone);
        // then
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findUserByPhoneNumber(userPhoneCaptor.capture());
        assertThat(userPhoneCaptor.getValue()).isEqualTo(userPhone);
        assertEquals(foundUser, user);
    }

    @Test
    void getUserByPhoneNumberThrowsExceptionIfUserWithGivenPhoneNumberDoesNotExists() {
        // given
        String userPhone = "345543345";
        User user = User.builder()
                .firstName("Steven")
                .email("stev@mail.com")
                .phoneNumber("345543345")
                .build();
        given(userRepository.findUserByPhoneNumber(userPhone)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.getUserByPhoneNumber(userPhone))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with phone number = " + userPhone + " not found");
        verify(userRepository).findUserByPhoneNumber(userPhoneCaptor.capture());
        assertThat(userPhoneCaptor.getValue()).isEqualTo(userPhone);
    }

    @Test
    void updateUserByIdUpdatesUser() throws ObjectNotFoundException, DuplicateUniqueValueException {
        // given
        Long userId = 1L;
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        User foundUser = User.builder()
                .firstName("Alan")
                .lastName("Crox")
                .birthDate(LocalDateTime.now())
                .gender(Gender.MALE)
                .phoneNumber("222333666")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(foundUser));
        given(userRepository.existsUserByPhoneNumber(user.getPhoneNumber())).willReturn(false);
        given(userRepository.save(foundUser)).willReturn(foundUser);
        // when
        User updatedUser = userService.updateUserById(userId, user);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).findById(userIdCaptor.capture());
        verify(userRepository).existsUserByPhoneNumber(userPhoneCaptor.capture());
        verify(userRepository).save(userCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(userPhoneCaptor.getValue()).isEqualTo(user.getPhoneNumber());
        assertThat(userCaptor.getValue()).isEqualTo(foundUser);
        assertEquals(updatedUser, user);
    }

    @Test
    void updateUserByIdThrowsExceptionIfUserWithGivenIdDoesNotExists() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> userService.updateUserById(userId, user))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with id = " + userId + " not found");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(userRepository, never()).existsUserByPhoneNumber(any());
        verify(userRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void updateUserByIdThrowsExceptionIfOtherUserWithSamePhoneNumberExists() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        User foundUser = User.builder()
                .firstName("Alan")
                .lastName("Crox")
                .birthDate(LocalDateTime.now())
                .gender(Gender.MALE)
                .phoneNumber("222333666")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(foundUser));
        given(userRepository.existsUserByPhoneNumber(user.getPhoneNumber())).willReturn(true);
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> userPhoneCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.updateUserById(userId, user))
                .isInstanceOf(DuplicateUniqueValueException.class)
                .hasMessageContaining("User with phone = " + user.getPhoneNumber() + " already exists");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(userRepository).existsUserByPhoneNumber(userPhoneCaptor.capture());
        verify(userRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(userPhoneCaptor.getValue()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    void updatePasswordByUserIdUpdatesUserPassword() throws ObjectNotFoundException, InvalidPasswordException {
        // given
        Long userId = 1L;
        String newPassword = "password";
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        // when
        userService.updatePasswordByUserId(userId, newPassword);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> userPasswordCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).findById(userIdCaptor.capture());
        verify(passwordEncoder).encode(userPasswordCaptor.capture());
        verify(userRepository).save(userCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
        assertThat(userPasswordCaptor.getValue()).isEqualTo(newPassword);
        assertThat(userCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void updatePasswordByUserIdThrowsExceptionIfUserWithGivenIdDoesNotExists() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> userService.updatePasswordByUserId(userId, any()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with id = " + userId + " not found");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void updatePasswordByUserIdThrowsExceptionIfGivenPasswordIsEmpty() {
        // given
        Long userId = 1L;
        String newPassword = "";
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> userService.updatePasswordByUserId(userId, newPassword))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessageContaining("User password cannot be blank");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void updatePasswordByUserIdThrowsExceptionIfGivenPasswordIsLongerThanHundredCharacters() {
        // given
        Long userId = 1L;
        String newPassword = "qwertqwertqwertqwetrtqwertqwertqwertwetrqwertqwertrrqwrfsdfkajandcjanc" +
                "sdklaksmdfkoasdfjansdfkjadckjasndcakjsdncajsdnckjasdnfcmalsdvnsdkjfncakjlsmgksdfmskmgfas";
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        assertThatThrownBy(() -> userService.updatePasswordByUserId(userId, newPassword))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessageContaining("User password cannot contain more than 100 characters");
        verify(userRepository).findById(userIdCaptor.capture());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void deleteUserByIdDeletesUser() throws ObjectNotFoundException {
        // given
        Long userId = 1L;
        given(userRepository.deleteUserById(userId)).willReturn(1);
        // when
        userService.deleteUserById(userId);
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).deleteUserById(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void deleteUserByIdThrowsExceptionIfUserWithGivenIdDoesNotExists() {
        // given
        Long userId = 1L;
        given(userRepository.deleteUserById(userId)).willReturn(0);
        // when
        // then
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        Assertions.assertThatThrownBy(() -> userService.deleteUserById(userId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User with id = " + userId + " not found");
        verify(userRepository).deleteUserById(userIdCaptor.capture());
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    }

    @Test
    void loadUserByUsernameReturnsUser() {
        // given
        String username = "user@mail.com";
        User user = User.builder()
                .firstName("Maria")
                .lastName("Beloc")
                .email(username)
                .birthDate(LocalDateTime.now())
                .gender(Gender.FEMALE)
                .phoneNumber("345654456")
                .build();
        given(userRepository.findUserByEmail(username)).willReturn(Optional.of(user));
        // when
        User foundUser = (User) userService.loadUserByUsername(username);
        // then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findUserByEmail(usernameCaptor.capture());
        assertThat(usernameCaptor.getValue()).isEqualTo(username);
        assertEquals(foundUser, user);
    }

    @Test
    void loadUserByUsernameThrowsExceptionIfUserWithGivenUsernameDoesNotExists() {
        // given
        String username = "user@mail.com";
        given(userRepository.findUserByEmail(username)).willReturn(Optional.empty());
        // when
        // then
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        assertThatThrownBy(() -> userService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with email = " + username + " not found");
        verify(userRepository).findUserByEmail(usernameCaptor.capture());
        assertThat(usernameCaptor.getValue()).isEqualTo(username);
    }
}