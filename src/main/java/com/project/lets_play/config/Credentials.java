package com.project.lets_play.config;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Credentials contient les informations de connexion d'un utilisateur pendant l'action login
 * Constructeur personnalisé pour éviter les données nulles
 * @param email
 * @param password
 */
public record Credentials(
    @NotBlank @Email String email,
    @NotBlank String password
) {}