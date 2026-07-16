package com.project.lets_play.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

import com.project.lets_play.repository.UserRepository;

import com.project.lets_play.errorhandling.UserNotFoundException;
import com.project.lets_play.model.User;

/**
 * UserService fait appel à toutes les fonctions de userRepository pour gérer les utilisateurs dans la base de données / API
 * Tous les controllers passent par un userService pour gérer les appels à la collection users
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

   
    private boolean validInput(String input) {
        if (input == null) {
            return false;
        }

        // Autorisés : alphanumérique, espaces + symboles (!, #, %, &, *, (, ), +, ,, -, ., /, :, ;, <, =, >, ?, @, ^, _, `, |, ~)
        // Interdits : symboles dangereux $, {, }, [, ], ", ', et \
        return Pattern.matches("^[a-zA-Z0-9 !#%&()*+,\\-./:;<=>?@^_`|~]+$", input);
    }

    public User createUser(User user) {
        if (!validInput(user.getEmail()) || !validInput(user.getPassword())) {
            throw new IllegalArgumentException("Erreur 422 [Unprocessable Content] : Caractères non autorisés dans le mot de passe ou l'email utilisateur.");
        }

        String rawPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(readUser(id));
    }

    public User readUser(String id) {        
        return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("Impossible de trouver l'utilisateur d'id " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("Impossible de trouver l'utilisateur à l'adresse " + email));
    }

    public User updateUser(User user) {
       return createUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
