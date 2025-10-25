package com.saver.UserService.service;

import com.saver.UserService.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String addUser(UserDto user);
    List<UserDto> getAllUsers();
    UserDto getUser(UserDto user);
    String deleteUser(UserDto user);
    UserDto updateUser(UserDto user);
}
