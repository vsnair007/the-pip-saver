package com.saver.UserService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    @NotNull(message = "User Name cannot be null")
    @NotBlank(message = "User Name cannot be blank")
    private String userName;

    private Long userId;

    private String password;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;

    @Email(message = "Invalid Email")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String emailId;
}
