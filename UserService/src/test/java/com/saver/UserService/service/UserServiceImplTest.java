package com.saver.UserService.service;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.exception.UserAlreadyExistException;
import com.saver.UserService.exception.UserNotFoundException;
import com.saver.UserService.model.Role;
import com.saver.UserService.model.User;
import com.saver.UserService.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .emailId("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        userDto = UserDto.builder()
                .userId(1L)
                .emailId("test@example.com")
                .password("plainPassword")
                .build();
    }

    @Test
    void addUser_Success() {
        when(userRepo.findByEmailId(userDto.getEmailId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(inv -> {
            User saved = inv.getArgument(0);
            saved.setUserId(1L);
            return saved;
        });
        String result = userService.addUser(userDto);
        assertEquals("Successfully Added", result);
        verify(userRepo).findByEmailId("test@example.com");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void addUser_AlreadyExists_ThrowsException() {
        when(userRepo.findByEmailId(userDto.getEmailId())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(userDto));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        User user2 = User.builder()
                .userId(2L)
                .emailId("abc@example.com")
                .build();

        when(userRepo.findAll()).thenReturn(Arrays.asList(user, user2));
        List<UserDto> result = userService.getAllUsers();
        assertEquals(2, result.size());
        verify(userRepo).findAll();
    }

    @Test
    void getUser_Found() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        UserDto result = userService.getUser(userDto);
        assertNotNull(result);
        assertEquals(userDto.getEmailId(), result.getEmailId());
        verify(userRepo).findById(1L);
    }

    @Test
    void getUser_NotFound_ThrowsException() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(userDto));
        verify(userRepo).findById(1L);
    }

    @Test
    void deleteUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        String result = userService.deleteUser(userDto);
        assertEquals("User Deleted Successfully", result);
        verify(userRepo).delete(user);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userDto));
        verify(userRepo, never()).delete(any(User.class));
    }

    @Test
    void updateUser_MissingId_ThrowsException() {
        userDto.setUserId(null);
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        userDto.setUserId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));
        verify(userRepo).findById(1L);
    }

    @Test
    void updateUser_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);
        UserDto result = userService.updateUser(userDto);
        assertNotNull(result);
        assertEquals(userDto.getEmailId(), result.getEmailId());
        verify(userRepo).save(any(User.class));
    }
}
