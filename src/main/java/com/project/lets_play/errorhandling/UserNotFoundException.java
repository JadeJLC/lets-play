package com.project.lets_play.errorhandling;

public class UserNotFoundException extends RuntimeException {
    private String message;

    public UserNotFoundException() {
        super("Impossible de trouver cet utilisateur. Veuillez réessayez.");
        this.message = "Impossible de trouver cet utilisateur. Veuillez réessayez.";
    }

    public UserNotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
