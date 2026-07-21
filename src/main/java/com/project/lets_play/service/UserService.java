package com.project.lets_play.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

import com.project.lets_play.repository.UserRepository;
import com.project.lets_play.utils.InputValidation;
import com.project.lets_play.errorhandling.UserAlreadyExistsException;
import com.project.lets_play.errorhandling.UserNotFoundException;
import com.project.lets_play.model.User;
import com.project.lets_play.model.UserResponse;

/**
 * UserService fait appel à toutes les fonctions de userRepository pour gérer les utilisateurs dans la base de données / API
 * Tous les controllers passent par un userService pour gérer les appels à la collection users
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InputValidation inputValidation;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, InputValidation inputValidation) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.inputValidation = inputValidation;
    }


    public UserResponse createUser(User user) {
        if (!inputValidation.areAllValid(false, user.getEmail(), user.getName(), user.getPassword())) {
            throw new IllegalArgumentException("Erreur 400 [Bad Request]: Caractères non autorisés dans le nom, le mot de passe ou l'email utilisateur.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
           throw new UserAlreadyExistsException();
        }

        String rawPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
         userRepository.save(user);

        return new UserResponse(user);
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

    public UserResponse updateUser(User userToUpdate, User user, boolean isAdmin) {

        if (!inputValidation.areAllValid(false, user.getEmail(), user.getName())) {
            throw new IllegalArgumentException("Erreur 400 [Bad Request]: Caractères non autorisés dans le nom ou l'email utilisateur.");
        }

        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        if (isAdmin) {
        userToUpdate.setRole(user.getRole());
        }
        
        userRepository.save(userToUpdate);
        return new UserResponse(userToUpdate);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
