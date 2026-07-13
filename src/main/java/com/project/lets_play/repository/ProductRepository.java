package com.project.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.project.lets_play.model.Product;;

/**
 * Repository basé sur MongoDB gérant les fonctionnalités de base (findByID, save, insert, etc)
 * Appelé par les services associés pour agir sur la collection "products"
 */
public interface ProductRepository extends MongoRepository<Product, String> {
    
}
