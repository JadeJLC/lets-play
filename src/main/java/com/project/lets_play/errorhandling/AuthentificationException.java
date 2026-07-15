package com.project.lets_play.errorhandling;

public class AuthentificationException extends RuntimeException {
    private String message;

    public AuthentificationException() {
        super("Erreur 401 [Unauthorized] : Seuls les utilisateurs connectés peuvent effectuer cette action. Connectez-vous et réessayez.");
        this.message = "Erreur 401 [Unauthorized] : Seuls les utilisateurs connectés peuvent effectuer cette action. Connectez-vous et réessayez.";
    }

    public AuthentificationException(String msg) {
        super("Erreur 401 [Unauthorized] : " + msg);
        this.message = "Erreur 401 [Unauthorized] : " + msg;
    }
}
