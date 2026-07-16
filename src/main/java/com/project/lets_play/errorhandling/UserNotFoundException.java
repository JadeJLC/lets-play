package com.project.lets_play.errorhandling;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Erreur 404 [Not Found] : Impossible de trouver cet utilisateur. Veuillez réessayez.");
    }

    public UserNotFoundException(String msg) {
        super("Erreur 404 [Not Found] : " + msg);
    }
}
