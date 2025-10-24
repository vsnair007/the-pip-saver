package com.saver.UserService.service;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.exception.UserAlreadyExistException;
import com.saver.UserService.model.Role;
import com.saver.UserService.model.User;
import com.saver.UserService.repo.UserRepo;
import com.saver.UserService.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public String addUser(UserDto user) throws RuntimeException {
        userRepo.findByEmailId(user.getEmailId()).ifPresent(ex -> {
            throw new UserAlreadyExistException("A user with same email(" + user.getEmailId() + ") already exist.");
        });
        User newUser = UserMapper.toEntity(user);
        if(newUser.getRole() == null){
            newUser.setRole(Role.USER);
        }
        newUser = userRepo.save(newUser);
        if (newUser.getUserId() != null && !newUser.getUserId().equals(0L)) {
            return "Successfully Added";
        }
        return "User Addition Failed";
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDto getUser(UserDto user) {
        return null;
    }

    @Override
    public String deleteUser(UserDto user) {
        return "";
    }
}
