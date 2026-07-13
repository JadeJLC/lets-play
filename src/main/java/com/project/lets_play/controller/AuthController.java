package com.project.lets_play.controller;

import org.springframework.web.bind.annotation.RestController;

import com.project.lets_play.service.AuthService;
import com.project.lets_play.config.Credentials;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Gestion des routes d'authentification via /auth
 * Prend un authService pour les fonctionnalités et les méthodes internes
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Gère la connexion via le chemin /login
    @PostMapping("/login")
    public String authentication(@RequestBody Credentials credentials) {
        return authService.login(credentials);
        
    }

    
}
