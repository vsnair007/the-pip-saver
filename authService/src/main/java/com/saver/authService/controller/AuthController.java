package com.saver.authService.controller;

import com.saver.authService.dto.AuthResponse;
import com.saver.authService.dto.UserDto;
import com.saver.authService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Service responsible for performing authentication operations (login, token validation).
     * Injected via constructor by Lombok's {@code @RequiredArgsConstructor}.
     */
    private final AuthService authService;

    /**
     * Authenticate a user and return a token string on success.
     *
     * @param userDto the user credentials (validated with {@code @Valid}); typical fields include username/email and password
     * @return {@link ResponseEntity} containing a token string (e.g. JWT) when authentication succeeds; appropriate error responses are handled by service/exceptions
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(authService.login(userDto));
    }

    /**
     * Validate an authorization token and return authentication details.
     *
     * <p>Expect the {@code Authorization} header value (commonly in the form {@code "Bearer <token>"}).
     * The controller forwards the header value to {@link AuthService#validateToken} for parsing and validation.</p>
     *
     * @param token the raw {@code Authorization} header value containing the token
     * @return {@link ResponseEntity} with an {@link AuthResponse} describing token validity and associated user info/claims
     */
    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String token) {
        System.out.println("Token received: " + token);
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
