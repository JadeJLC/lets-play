package com.project.lets_play.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.project.lets_play.repository.ProductRepository;
import com.project.lets_play.utils.InputValidation;
import com.project.lets_play.errorhandling.ProductNotFoundException;
import com.project.lets_play.model.Product;

/**
 * ProductService fait appel à toutes les fonctions de productRepository pour gérer les produits dans la base de données / API
 * Tous les controllers passent par un productService pour gérer les appels à la collection products
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final InputValidation inputValidation;

    public ProductService(ProductRepository productRepository, InputValidation inputValidation) {
        this.productRepository = productRepository;
        this.inputValidation = inputValidation;
    }

    public Product createProduct(Product product) {
        if (!inputValidation.areAllValid(false, product.getName(), product.getUserId()) || !inputValidation.isValidNumber(product.getPrice()) || ! inputValidation.isValidText(product.getDescription(), true)) {
            throw new IllegalArgumentException("Erreur 400 [Bad Request] : Erreur dans les champs de produit : la description, le prix ou le nom est incorrect.");
        }
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

    public Product updateProduct(Product productToUpdate, Product product) {
        if (!inputValidation.isValidText(product.getName(), false) || !inputValidation.isValidNumber(product.getPrice()) || ! inputValidation.isValidText(product.getDescription(), true)) {
            throw new IllegalArgumentException("Erreur 400 [Bad Request] : Erreur dans les champs de produit : la description, le prix ou le nom est incorrect.");
        }

        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());        

       return  productRepository.save(productToUpdate);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
}
