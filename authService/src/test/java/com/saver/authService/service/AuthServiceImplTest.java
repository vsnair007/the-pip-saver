package com.saver.authService.service;

import com.saver.authService.dto.AuthResponse;
import com.saver.authService.dto.UserDto;
import com.saver.authService.exception.UserNotFoundException;
import com.saver.authService.model.Role;
import com.saver.authService.model.User;
import com.saver.authService.repo.AuthRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepo authRepo;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_success_returnsToken() {
        // Arrange
        User user = User.builder()
                .password("encodedPwd")
                .emailId("user@example.com")
                .build();
        user.setRole(Role.USER);

        UserDto dto = new UserDto();
        dto.setUserId(1L);
        dto.setPassword("rawPwd");

        when(authRepo.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPwd", "encodedPwd")).thenReturn(true);
        when(jwtService.generateToken("user@example.com", "USER")).thenReturn("token123");

        // Act
        String token = authService.login(dto);

        // Assert
        assertEquals("token123", token);
        verify(jwtService).generateToken("user@example.com", "USER");
        verify(authRepo).findById(1L);
    }

    @Test
    void login_userNotFound_throwsUserNotFoundException() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setUserId(1L);

        when(authRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authService.login(dto));
        verify(authRepo).findById(1L);
    }

    @Test
    void login_badCredentials_throwsBadCredentialsException() {
        // Arrange
        User user = User.builder()
                .password("encodedPwd")
                .build();

        UserDto dto = new UserDto();
        dto.setUserId(1L);
        dto.setPassword("wrongPwd");

        when(authRepo.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(dto));
        verify(authRepo).findById(1L);
    }

    @Test
    void validateToken_withBearer_validToken_returnsAuthResponse() {
        // Arrange
        String header = "Bearer token123";

        when(jwtService.validateToken("token123")).thenReturn(true);
        when(jwtService.extractEmail("token123")).thenReturn("user@example.com");
        when(jwtService.extractRole("token123")).thenReturn("USER");

        // Act
        AuthResponse resp = authService.validateToken(header);

        // Assert
        assertTrue(resp.getValid());
        assertEquals("user@example.com", resp.getEmail());
        assertEquals("USER", resp.getRole());
        verify(jwtService).validateToken("token123");
    }

    @Test
    void validateToken_invalidToken_returnsInvalidResponse() {
        // Arrange
        String header = "Bearer token-invalid";
        when(jwtService.validateToken("token-invalid")).thenReturn(false);

        // Act
        AuthResponse resp = authService.validateToken(header);

        // Assert
        assertFalse(resp.getValid());
        assertNull(resp.getEmail());
        assertNull(resp.getRole());
        verify(jwtService).validateToken("token-invalid");
    }
}

