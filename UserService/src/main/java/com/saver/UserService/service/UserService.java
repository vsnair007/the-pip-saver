package com.saver.UserService.service;

import com.saver.UserService.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public String addUser(UserDto user);
    public List<UserDto> getAllUsers();
    public UserDto getUser(UserDto user);
    public String deleteUser(UserDto user);
}
