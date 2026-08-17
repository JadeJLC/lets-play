package com.project.lets_play.config;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * UserClaims contient les données d'autorisation d'accès de l'utilisateur, récupérées dans le token
 * Constructeur personnalisé pour éviter les données nulles
 * @param email {String} 
 * @param role {String} admin/user
 */
public record UserClaims(@NotBlank @Email String email, @NotBlank String role) {
    public UserClaims {
        Objects.requireNonNull(email);
        Objects.requireNonNull(role);
    }
}