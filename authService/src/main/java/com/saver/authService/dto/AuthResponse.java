package com.saver.authService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private Boolean valid;
    private String role;
    private String email;
}
