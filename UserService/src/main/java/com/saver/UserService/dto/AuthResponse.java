package com.saver.UserService.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private Boolean valid;
    private String role;
    private String email;
}
