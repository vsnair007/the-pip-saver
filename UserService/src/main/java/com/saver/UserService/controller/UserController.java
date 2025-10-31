package com.saver.UserService.controller;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    /**
     * Service that contains user-related business logic.
     * Injected via constructor by Lombok's `@RequiredArgsConstructor`.
     */
    private final UserService userService;

    /**
     * Create a new user.
     *
     * @param userDto validated DTO containing user details
     * @return ResponseEntity with a success message or created resource identifier
     */
    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    /**
     * Retrieve a single user by id.
     *
     * Note: the controller accepts the id as a string and converts it to a `Long`
     * before delegating to the service.
     *
     * @param id user id as string (will be parsed to Long)
     * @return ResponseEntity containing the found UserDto
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Retrieve a single user by id")
    @ApiResponse(responseCode = "200", description = "User found and returned")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(UserDto.builder().userId(Long.parseLong(id)).build()));
    }

    /**
     * Retrieve all users.
     *
     * @return ResponseEntity containing a list of UserDto objects
     */
    @GetMapping
    @Operation(summary = "List users", description = "Retrieve all users")
    @ApiResponse(responseCode = "200", description = "List of users returned")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Delete a user by id.
     *
     * Note: the controller accepts the id as a string and converts it to a `Long`
     * before delegating to the service.
     *
     * @param id user id as string (will be parsed to Long)
     * @return ResponseEntity with a status message about the deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by id")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.deleteUser(UserDto.builder().userId(Long.parseLong(id)).build()));
    }

    /**
     * Update an existing user.
     *
     * @param userDto validated DTO containing updated user details
     * @return ResponseEntity containing the updated UserDto
     */
    @PutMapping
    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "User updated and returned")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

}
