package com.project.lets_play.errorhandling;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Erreur 400 [Bad Request] : Identifiant ou mot de passe incorrect. Veuillez réessayer.");
    }

    public AuthenticationException(String msg) {
        super("Erreur 400 [Bad Request] : " + msg);
    }
}
