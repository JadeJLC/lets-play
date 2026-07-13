package com.project.lets_play.config;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.project.lets_play.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Classe pour la gestion complète des tokens de connexion
 */
@Configuration
public class JWT {
    @Value("${jwt.secret}")
    private String secretKey;

    public SecretKey generateKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Création d'un token d'identification pour l'utilisateur connecté
     * Récupère la date actuelle et la date d'expiration + le nom, le rôle et l'email de l'utilisateur
     * Encode le tout avec la clé secrète de l'API
     * @param user {User}
     * @return le token sous forme de String complète
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiDate = addHoursToJavaUtilDate(now, 1);
        

        return Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(now)
        .expiration(expiDate)
        .claim("role", user.getRole())
        .signWith(generateKey(secretKey))
        .compact();
    }

    // Fonction d'assistance pour generateToken pour calculer l'expiration du token
    private Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


    /**
     * Fonction de récupération des données d'identification : role et email, après vérification de sa validité. 
     * Renvoie .empty() si le token est invalide
     * @param compact le token complet à découper
     * @return UserClaims
     */
    public Optional<UserClaims> extractTokenData(CharSequence compact) {
        Claims claims = isTokenValid(compact);
        if (claims == null) {
            return Optional.empty();
        }

        String email = claims.getSubject(); 
        String role = claims.get("role", String.class);

        return Optional.of(new UserClaims(email, role));
    }

    /**
     * Fonction d'assistance de extractTokenData servant à vérifier la validité du token :
     * Clé de sécurité et date d'expiration
     * @param compact le token complet à vérifier
     * @return UserClaims ou null selon la validité du token
     */
    private Claims isTokenValid(CharSequence compact) {
        try {
        Jws<Claims> claimsJws = Jwts.parser()
            .verifyWith(generateKey(secretKey)) 
            .build()
            .parseSignedClaims(compact); 

        Claims claims = claimsJws.getPayload();
        return claims;

    } catch (JwtException | IllegalArgumentException e) {
        System.out.println("Token validation failed: " + e.getMessage());
        return null;
    }
    }
}
