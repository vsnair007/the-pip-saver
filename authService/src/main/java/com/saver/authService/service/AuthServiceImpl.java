package com.saver.authService.service;

import com.saver.authService.dto.UserDto;
import com.saver.authService.exception.UserNotFoundException;
import com.saver.authService.model.User;
import com.saver.authService.repo.AuthRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthRepo authRepo;

    private final JwtService jwtService;

    @Override
    public String login(UserDto userDto) {
        User user = authRepo.findById(userDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return jwtService.generateToken(user.getEmailId(), String.valueOf(user.getRole()));
    }

    @Override
    public String validateToken(String token) {
        return "";
    }
}
