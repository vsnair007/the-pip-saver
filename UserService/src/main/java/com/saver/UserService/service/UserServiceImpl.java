package com.saver.UserService.service;

import com.saver.UserService.dto.UserDto;
import com.saver.UserService.exception.UserAlreadyExistException;
import com.saver.UserService.exception.UserNotFoundException;
import com.saver.UserService.model.Role;
import com.saver.UserService.model.User;
import com.saver.UserService.repo.UserRepo;
import com.saver.UserService.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Repository used for CRUD operations on User entities.
     */
    private final UserRepo userRepo;

    /**
     * Password encoder used to hash user passwords before persisting.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Add a new user to the system.
     *
     * Process:
     * - Ensure no existing user has the same email.
     * - Convert the provided {@link UserDto} to a {@link User} entity.
     * - Encode the password and set a default role if none provided.
     * - Persist the new user and return a simple status message.
     *
     * @param user DTO containing user details to create
     * @return success message when user is created, otherwise a failure message
     * @throws UserAlreadyExistException when a user with the same email already exists
     */
    @Override
    public String addUser(UserDto user) throws RuntimeException {
        userRepo.findByEmailId(user.getEmailId()).ifPresent(ex -> {
            throw new UserAlreadyExistException("A user with same email(" + user.getEmailId() + ") already exist.");
        });
        User newUser = UserMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if (newUser.getRole() == null) {
            newUser.setRole(Role.USER);
        }
        newUser.setUserId(null);
        newUser = userRepo.save(newUser);
        if (newUser.getUserId() != null && !newUser.getUserId().equals(0L)) {
            return "Successfully Added";
        }
        return "User Addition Failed";
    }

    /**
     * Retrieve all users from the system.
     *
     * The returned list is sorted according to the natural ordering of the
     * underlying entity and mapped to {@link UserDto} objects.
     *
     * @return list of all users as DTOs
     */
    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().sorted().map(UserMapper::toDto).toList();
    }

    /**
     * Retrieve a single user by id.
     *
     * @param user DTO containing the userId to look up
     * @return the found user as a {@link UserDto}
     * @throws UserNotFoundException when no user exists for the provided id
     */
    @Override
    public UserDto getUser(UserDto user) {
        return userRepo.findById(user.getUserId()).map(UserMapper::toDto).orElseThrow(() -> new UserNotFoundException("No user found with ID: " + user.getUserId()));
    }

    /**
     * Delete a user by id.
     *
     * The method verifies that the user exists before deleting.
     *
     * @param user DTO containing the userId to delete
     * @return confirmation message after successful deletion
     * @throws UserNotFoundException when no user exists for the provided id
     */
    @Override
    public String deleteUser(UserDto user) {
        User existingUser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException(
                        "No user found with ID: " + user.getUserId()
                ));

        userRepo.delete(existingUser);
        return "User Deleted Successfully";
    }

    /**
     * Update an existing user.
     *
     * Requirements:
     * - The DTO must include a valid non-zero userId.
     * - The user must exist in the repository before update.
     *
     * @param user DTO containing updated user information
     * @return the updated user as a {@link UserDto}
     * @throws UserNotFoundException when the provided userId is missing or the user does not exist
     */
    @Override
    public UserDto updateUser(UserDto user) {
        if (user.getUserId() == null || user.getUserId().equals(0L)) {
            throw new UserNotFoundException("User ID must be provided for update.");
        }
        userRepo.findById(user.getUserId())
                .ifPresent(ex -> {
                    throw new UserNotFoundException("A user with Id(" + user.getUserId() + ") not found.");
                });
        return UserMapper.toDto(userRepo.save(UserMapper.toEntity(user)));

    }

}
