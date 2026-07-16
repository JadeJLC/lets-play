package com.project.lets_play.errorhandling;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Erreur 403 [Forbidden] : Impossible de créer cet utilisateur. Ce compte existe déjà ou des données sont manquantes ou erronées.");
    }

    public UserAlreadyExistsException(String msg) {
        super("Erreur 403 [Forbidden] : " + msg);
    }
}
