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

import java.util.ArrayList;
import java.util.List;

import com.project.lets_play.service.AuthService;
import com.project.lets_play.service.UserService;
import com.project.lets_play.model.User;
import com.project.lets_play.model.UserResponse;
import com.project.lets_play.errorhandling.UnauthorizedOperationException;
import com.project.lets_play.errorhandling.UserAlreadyExistsException;
import com.project.lets_play.errorhandling.UserNotFoundException;

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
    public UserResponse createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id) {
        User user = userService.readUser(id);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return new UserResponse(user);
    }

    @PutMapping
    public UserResponse updateUser(@RequestBody User user, Authentication authentication) {
        if (!authService.isAuthorized(user.getEmail(), authentication)) {
            throw new UnauthorizedOperationException("Vous n'êtes pas autorisé à modifier cet utilisateur.");
        } 

        User userToUpdate = userService.readUser(user.getId());
        return userService.updateUser(userToUpdate, user, authService.isAdmin(authentication));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id, Authentication authentication) {
        String email = getUser(id).getEmail();

        if (authService.isAuthorized(email, authentication)) {
        userService.deleteUser(id);
        } else {
            throw new UnauthorizedOperationException("Vous n'êtes pas autorisé à supprimer cet utilisateur.");
        }
        return;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(Authentication authentication) {
        if (authService.isAdmin(authentication)) {
           List<User> users = userService.getAllUsers();
           List<UserResponse> filteredUsers = new ArrayList<UserResponse>();

          for (User user : users) {
            UserResponse reponse = new UserResponse(user);
            filteredUsers.add(reponse);
          }

          return filteredUsers;
        } else {
            throw new UnauthorizedOperationException("Seul un administrateur peut effectuer cette action.");
        }
    }
    
}