package com.project.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.project.lets_play.model.User;

import java.util.Optional;

/**
 * Repository basé sur MongoDB gérant les fonctionnalités de base (findByID, save, insert, etc)
 * Appelé par les services associés pour agir sur la collection "users"
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Méthode personnalisée pour trouver un utilisateur via son email, utilisé notamment pour la connexion
     * @param email {String}
     * @return un Optional, soit .empty() si aucun résultat, soit User
     */    
    Optional<User> findByEmail(String email);
}
