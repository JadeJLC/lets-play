package com.project.lets_play.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.lets_play.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

/**
 * Filtrage des requêtes HTML selon les autorisations de l'utilisateur
 * OncePerRequestFilter garantit que le filtre ne sera appelé qu'une seule fois par requête
 * (En cours de conception)
 */
@Component
public class AuthFilter extends OncePerRequestFilter{
    private JWT jwtToken;

    public AuthFilter(JWT jwToken) {
        this.jwtToken = jwToken;        
    }
    
    /**
     * Méthode de vérification des autorisations de l'utilisateur via lecture du token
     * @param request Requête HTML dont sera extraite le token
     * @param filterChain Suite de la chaîne de vérification
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {
        // Récupération du token
        String extractedHeader = request.getHeader("Authorization");
        if (extractedHeader != null && extractedHeader.startsWith("Bearer ")) {
            extractedHeader = extractedHeader.substring(7);
            Optional<UserClaims> userClaimsOpt = jwtToken.extractTokenData(extractedHeader);  

            userClaimsOpt.ifPresent(claims -> {
                // Génère des informations de role pour SpringSecurity, qui demande un type spécifique
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(claims.role())
                );
    
                 // Envoie le mail et le rôle à SpringSecurity, sans le mot de passe (plus nécessaire)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    claims.email(), 
                    null, 
                    authorities
                );
    
                // Fabrique le token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            });
        }
        

        // Une fois le filtre fini, on passe aux étapes de vérification suivantes
        filterChain.doFilter(request, response); 
    }    
}
