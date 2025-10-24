package com.saver.UserService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String userName;
    private Long userId;
    private String password;
    private String phoneNumber;
    private String emailId;
}
