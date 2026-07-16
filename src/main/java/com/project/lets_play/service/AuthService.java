package com.project.lets_play.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.project.lets_play.config.JWT;
import com.project.lets_play.errorhandling.AuthenticationException;
import com.project.lets_play.config.Credentials;
import com.project.lets_play.service.UserService;
import com.project.lets_play.model.User;

/**
 * Gestion complète de l'authentification des utilisateurs
 * Fonctionne avec le passwordEncoder, un token et un userService
 */
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

    /**
     * Gestion de la connexion d'un utilisateur via son email et son mot de passe
     * @param credentials 
     * @return le token de connexion ou null si l'utilisateur n'est pas trouvé
     */
    public String login(Credentials credentials) {
        User user = userService.findByEmail(credentials.email());
        if (user == null) {
            throw new AuthenticationException();
        }

        if (passwordEncoder.matches(credentials.password(), user.getPassword())) {
            return jwToken.generateToken(user);
        } else {
            throw new AuthenticationException();
        }

    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                             .stream()
                             .anyMatch(role -> role.getAuthority().equals("admin"));
    }

    public boolean isAuthorized(String email, Authentication authentication) {
        String requesterEmail = authentication.getName();
        return requesterEmail.equals(email) || isAdmin(authentication);
    }

    public boolean isIdAuthorized(String id, Authentication authentication) {
        String email = userService.readUser(id).getEmail();
        String requesterEmail = authentication.getName();
        return requesterEmail.equals(email) || isAdmin(authentication);
    }

}
