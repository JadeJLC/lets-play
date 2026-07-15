package com.project.lets_play.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.project.lets_play.service.AuthService;
import com.project.lets_play.service.UserService;
import com.project.lets_play.model.User;

/**
 * Gestion des routes d'accès aux utilisateurs via /users
 * Prend un userervice pour les fonctionnalités et les méthodes internes
 * Implémentation de CRUD (Create, Read, Update, Delete) pour chaque utilisateur + récupération de la liste complète
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.readUser(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User user, Authentication authentication) {
        if (authService.isAuthorized(user.getEmail(), authentication)) {
            return userService.updateUser(user);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id, Authentication authentication) {
        String email = getUser(id).getEmail();

        if (authService.isAuthorized(email, authentication)) {
        userService.deleteUser(id);
        }
        return;
    }

    @GetMapping
    public List<User> getAllUsers(Authentication authentication) {
        if (authService.isAdmin(authentication)) {
            return userService.getAllUsers();
        } else {
            return null;
        }
    }
    
}