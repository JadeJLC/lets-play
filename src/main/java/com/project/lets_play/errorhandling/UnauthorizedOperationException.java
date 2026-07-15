package com.project.lets_play.errorhandling;

public class UnauthorizedOperationException extends RuntimeException {
    private String message;

    public UnauthorizedOperationException() {
        super("Erreur 403 [Forbidden] : Vous n'avez pas les permissions nécessaires pour effectuer cette action.");
        this.message = "Erreur 403 [Forbidden] : Vous n'avez pas les permissions nécessaires pour effectuer cette action.";
    }

    public UnauthorizedOperationException(String msg) {
        super("Erreur 403 [Forbidden] : " + msg);
        this.message = "Erreur 403 [Forbidden] : " + msg;
    }
}
