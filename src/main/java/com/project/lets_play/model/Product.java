package com.project.lets_play.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Classe Products conforme au diagramme de classe fourni
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

    @Field @NotBlank @Size(min = 3)
    private String name;

    @Field 
    private String description;

    @Field @NotNull
    private Double price;

    @Field
    private String userId;
}
