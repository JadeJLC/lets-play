package com.project.lets_play.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.project.lets_play.service.AuthService;
import com.project.lets_play.service.ProductService;
import com.project.lets_play.errorhandling.UnauthorizedOperationException;
import com.project.lets_play.model.Product;

/**
 * Gestion des routes d'accès auxs produits via /products
 * Prend un productService pour les fonctionnalités et les méthodes internes
 * Implémentation de CRUD (Create, Read, Update, Delete) pour chaque produit + récupération de la liste complète
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product, Authentication authentication) {
        String ownerId = product.getUserId();

        if (authService.isIdAuthorized(ownerId, authentication)) {
            return productService.createProduct(product);
        } else {
            throw new UnauthorizedOperationException();
        }
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.readProduct(id);
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product product, Authentication authentication) {
        String ownerId = product.getUserId();

        if (authService.isIdAuthorized(ownerId, authentication)) {
            return productService.updateProduct(product);
        } else {
           throw new UnauthorizedOperationException("Vous n'êtes pas autorisé à modifier ce produit.");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id, Authentication authentication) {
        String ownerId = getProduct(id).getUserId();

        if (authService.isIdAuthorized(ownerId, authentication)) {
        productService.deleteProduct(id);
        } else {
            throw new UnauthorizedOperationException("Vous n'êtes pas autorisé à supprimer ce produit.");
        }
        return;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    
}