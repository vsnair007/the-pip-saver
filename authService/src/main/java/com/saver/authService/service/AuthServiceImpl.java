package com.saver.authService.service;


import com.saver.authService.dto.AuthResponse;
import com.saver.authService.dto.UserDto;
import com.saver.authService.exception.UserNotFoundException;
import com.saver.authService.model.User;
import com.saver.authService.repo.AuthRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepo authRepo;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(UserDto userDto) {
        User user = authRepo.findById(userDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmailId(), String.valueOf(user.getRole()));
    }

    @Override
    public AuthResponse validateToken(String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setValid(false);
        token = token.replace("Bearer ", "").trim();
        if (jwtService.validateToken(token)) {
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);
            authResponse.setValid(true);
            authResponse.setEmail(email);
            authResponse.setRole(role);
        }
        System.out.println("AuthResponse: " + authResponse.getValid() + " " + authResponse.getEmail() + " " + authResponse.getRole());
        return authResponse;
    }
}
