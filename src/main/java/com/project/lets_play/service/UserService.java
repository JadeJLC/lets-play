package com.project.lets_play.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import com.project.lets_play.repository.UserRepository;
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

    public User createUser(User user) {
        String rawPassword = user.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(readUser(id));
        return;
    }

    public User readUser(String id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
    }

    public User updateUser(User user) {
       return  createUser(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
}
