package com.project.lets_play.config;

import org.springframework.web.filter.OncePerRequestFilter;

import com.project.lets_play.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filtrage des requêtes HTML selon les autorisations de l'utilisateur
 * (En cours de conception)
 */
public class AuthFilter extends OncePerRequestFilter{
    private JWT jwtToken;

    public AuthFilter(JWT jwToken) {
        this.jwtToken = jwToken;        
    }
    
    /**
     * Méthode de vérification des autorisations de l'utilisateur via lecture du token
     * @param request Requête HTML dont sera extraite le token
     * @param response 
     * @param filterChain Suite de la chaîne de vérification
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String extractedHeader = request.getHeader("Authorization");
        if (extractedHeader != null && extractedHeader.startsWith("Bearer")) {
        extractedHeader = extractedHeader.substring(7);
        jwtToken.extractTokenData(extractedHeader);        
        }


        filterChain.doFilter(request, response); 
    }
    
}
