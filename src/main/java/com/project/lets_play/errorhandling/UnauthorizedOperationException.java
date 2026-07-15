package com.project.lets_play.errorhandling;

public class UnauthorizedOperationException extends RuntimeException {
    private String message;

    public UnauthorizedOperationException() {
        super("Vous n'êtes pas autorisé à effectuer cette action.");
        this.message = "Vous n'êtes pas autorisé à effectuer cette action.";
    }

    public UnauthorizedOperationException(String msg) {
        super(msg);
        this.message = msg;
    }
}
