package com.saver.UserService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@Builder
public class User {

    @NotBlank
    private String userName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId = 0L;

    private String password;

    @Pattern(regexp = "[0-9]{10}")
    private String phoneNumber;

    @Email
    @NotBlank
    private String emailId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
