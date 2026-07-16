package com.project.lets_play.errorhandling;

public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException() {
        super("Erreur 404 [Not Found] : Impossible de trouver ce produit. Veuillez réessayez.");
    }

    public ProductNotFoundException(String msg) {
        super("Erreur 404 [Not Found] : " + msg);
    }
}
