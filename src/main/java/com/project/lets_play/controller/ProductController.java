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
import com.project.lets_play.service.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

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
    private final UserService userService;

    public ProductController(ProductService productService, AuthService authService, UserService userService) {
        this.productService = productService;
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product, Authentication authentication) {
        String ownerEmail = authentication.getName();
        String ownerId = userService.findByEmail(ownerEmail).getId();

        
        product.setUserId(ownerId);
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.readProduct(id);
    }

    @PutMapping
    public Product updateProduct(@Valid @RequestBody Product product, Authentication authentication) {
        Product productToUpdate = productService.readProduct(product.getId());
        String ownerId = productToUpdate.getUserId();

        if (authService.isIdAuthorized(ownerId, authentication)) {
            return productService.updateProduct(productToUpdate, product);
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
    @PermitAll
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    
}