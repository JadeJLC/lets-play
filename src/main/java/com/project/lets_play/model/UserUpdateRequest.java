package com.project.lets_play.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank @Email
    private String email;

    private String role;
}