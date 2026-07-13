package com.project.lets_play.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.lets_play.config.JWT;
import com.project.lets_play.service.UserService;
import com.project.lets_play.model.User;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JWT jwToken;
    private final UserService userService;
    

    public AuthService(PasswordEncoder passwordEncoder, JWT jwToken, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.jwToken = jwToken;
        this.userService = userService;
    }

    public String login(String email, String password) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwToken.generateToken(user);
        } else {
            return null;
        }

    }

}
