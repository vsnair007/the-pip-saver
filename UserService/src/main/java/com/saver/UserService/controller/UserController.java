package com.saver.UserService.controller;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @GetMapping("/{id})")
    public ResponseEntity<UserDto> getUser(@PathVariable @NotNull @NotBlank @Valid String id) {
        return ResponseEntity.ok(userService.getUser(UserDto.builder().userId(Long.getLong(id)).build()));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable @NotNull @NotBlank @Valid String id) {
        return ResponseEntity.ok(userService.deleteUser(UserDto.builder().userId(Long.getLong(id)).build()));
    }

}
