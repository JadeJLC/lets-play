package com.project.lets_play.errorhandling;

public class UserAlreadyExistsException extends RuntimeException {
    private String message;

    public UserAlreadyExistsException() {
        super("Cet utilisateur existe déjà. Essayez avec d'autres identifiants");
        this.message = "Cet utilisateur existe déjà. Essayez avec d'autres identifiants";
    }

    public UserAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
