package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

/**
 * Classe User conforme au diagramme de classe fourni
 * Chaque produit un un id unique, un nom, une description et un prix
 * userId définit l'utilisateur propriétaire de l'objet via son id unique
 * @Document(collection) est un indicateur pour MongoDB pour créer une nouvelle collection/table de données "products"
 * @Data est une fonctionnalité Lombok pour générer automatiquement des getters, toString et equals pour la classe (code plus court)
 */
@Document(collection = "products") 
@Data
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private String userId;
}
