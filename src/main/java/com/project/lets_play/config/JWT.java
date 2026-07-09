package com.project.lets_play.config;

import org.springframework.beans.factory.annotation.Value;

public class JWT {
    @Value
    private final String secretKey = jwt.secret;
    
    public String generateToken() {
        return "";
    }

    public void extractTokenData() {}

    public boolean isTokenValid() {
        return false;
    }
}
