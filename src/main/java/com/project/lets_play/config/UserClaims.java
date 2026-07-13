package com.project.lets_play.config;

import java.util.Objects;

public record UserClaims(String email, String role) {
    public UserClaims {
        Objects.requireNonNull(email);
        Objects.requireNonNull(role);
    }
}