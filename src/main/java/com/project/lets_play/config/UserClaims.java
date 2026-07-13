package com.project.lets_play.config;

import java.util.Objects;

/**
 * UserClaims contient les données d'autorisation d'accès de l'utilisateur, récupérées dans le token
 * Constructeur personnalisé pour éviter les données nulles
 * @param email {String} 
 * @param role {String} admin/user
 */
public record UserClaims(String email, String role) {
    // S'assure que le mail et le rôle envoyés ne sont jamais nuls
    public UserClaims {
        Objects.requireNonNull(email);
        Objects.requireNonNull(role);
    }
}