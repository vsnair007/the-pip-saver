package com.saver.UserService.util;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.model.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .emailId(user.getEmailId())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .userName(dto.getUserName())
                .userId(dto.getUserId())
                .emailId(dto.getEmailId())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

}
