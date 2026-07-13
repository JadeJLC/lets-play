package com.project.lets_play.config;

import java.util.Objects;

/**
 * Credentials contient les informations de connexion d'un utilisateur pendant l'action login
 * Constructeur personnalisé pour éviter les données nulles
 * @param email
 * @param password
 */
public record Credentials(String email, String password) {
    public Credentials {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}