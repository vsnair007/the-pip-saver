package com.saver.authService.controller;

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

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(authService.login(userDto));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        System.out.println("Validating token: " + token);
        return ResponseEntity.ok(true);
        //return ResponseEntity.ok(authService.validateToken(token));
    }
}
