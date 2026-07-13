package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

/**
 * Classe User conforme au diagramme de classe fourni
 * Contient un id unique et un nom. <email> et <password> servent à la connexion
 * <role> définit les autorisations d'accès
 * @Document(collection) est un indicateur pour MongoDB pour créer une nouvelle collection/table de données "users"
 * @Data est une fonctionnalité Lombok pour générer automatiquement des getters, toString et equals pour la classe (code plus court)
 */
@Document(collection = "users") 
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
}
