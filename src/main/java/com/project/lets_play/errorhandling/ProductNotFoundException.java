package com.project.lets_play.errorhandling;

public class ProductNotFoundException extends RuntimeException {
    private String message;

    public ProductNotFoundException() {
        super("Impossible de trouver ce produit. Veuillez réessayez.");
        this.message = "Impossible de trouver ce produit. Veuillez réessayez.";
    }

    public ProductNotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
