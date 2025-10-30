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

    /**
     * Repository used to load user information from the persistence layer.
     */
    private final AuthRepo authRepo;

    /**
     * Service responsible for JWT creation, parsing and validation.
     */
    private final JwtService jwtService;

    /**
     * Password encoder used to verify raw credentials against stored hashed passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate a user using the provided credentials in {@code userDto}.
     *
     * Process:
     * 1. Load the {@link User} from the repository by id.
     * 2. Verify the provided raw password against the stored encoded password using {@link PasswordEncoder#matches}.
     * 3. If verification succeeds, generate and return a JWT via {@link JwtService#generateToken}.
     *
     * @param userDto DTO containing user identifier and raw password
     * @return generated JWT token string for the authenticated user
     * @throws UserNotFoundException when no user exists for the provided id
     * @throws BadCredentialsException when the provided password does not match the stored password
     */
    @Override
    public String login(UserDto userDto) {
        User user = authRepo.findById(userDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(user.getEmailId(), String.valueOf(user.getRole()));
    }

    /**
     * Validate a JWT token and return an {@link AuthResponse} describing its validity and claims.
     *
     * The method accepts a token string which may include the "Bearer " prefix; this prefix
     * will be removed if present. If the token is valid according to {@link JwtService#validateToken},
     * the email and role are extracted and populated in the returned {@link AuthResponse}.
     *
     * @param token raw authorization header or token string (may contain "Bearer " prefix)
     * @return {@link AuthResponse} indicating whether the token is valid and providing extracted claims when valid
     */
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
