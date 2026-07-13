package com.project.lets_play.config;

import java.util.Objects;

public record Credentials(String email, String password) {
    public Credentials {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}