package com.saver.authService.service;

import com.saver.authService.dto.AuthResponse;
import com.saver.authService.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public String login(UserDto userDto);
    public AuthResponse validateToken(String token);
}
