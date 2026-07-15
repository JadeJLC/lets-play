package com.project.lets_play.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.project.lets_play.repository.ProductRepository;
import com.project.lets_play.errorhandling.ProductNotFoundException;
import com.project.lets_play.model.Product;

/**
 * ProductService fait appel à toutes les fonctions de productRepository pour gérer les produits dans la base de données / API
 * Tous les controllers passent par un productService pour gérer les appels à la collection products
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.delete(readProduct(id));
        return;
    }

    public Product readProduct(String id) {
        return productRepository.findById(id).orElseThrow(
            () -> new ProductNotFoundException("Impossible de trouver le produit d'id " + id ));
    }

    public Product updateProduct(Product product) {
       return  createProduct(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
}
