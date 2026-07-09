package com.project.lets_play;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.lets_play.config.JWT;
import com.project.lets_play.model.User;

@SpringBootTest
public class JWTTests {

    @Autowired
    private JWT jwt;

    @Test
    public void generateToken_shouldReturnNonEmptyToken() {
        User user = new User();
        user.setEmail("jean@test.com");
        user.setName("Jean Test");
        user.setRole("user");

        String token = jwt.generateToken(user);

        assertNotNull(token, "Le token ne devrait pas être null");
        assertFalse(token.isEmpty(), "Le token ne devrait pas être vide");
        System.out.println("Token généré : " + token);
    }
}