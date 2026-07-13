package com.project.lets_play.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    

}