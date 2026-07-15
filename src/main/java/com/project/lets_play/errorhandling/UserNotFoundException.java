package com.project.lets_play.errorhandling;

public class UserNotFoundException extends RuntimeException {
    private String message;

    public UserNotFoundException() {
        super("Erreur 404 [Not Found] : Impossible de trouver cet utilisateur. Veuillez réessayez.");
        this.message = "Erreur 404 [Not Found] : Impossible de trouver cet utilisateur. Veuillez réessayez.";
    }

    public UserNotFoundException(String msg) {
        super("Erreur 404 [Not Found] : " + msg);
        this.message = "Erreur 404 [Not Found] : " + msg;
    }
}
