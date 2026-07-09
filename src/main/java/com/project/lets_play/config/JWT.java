package com.project.lets_play.config;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.project.lets_play.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JWT {
    @Value("${jwt.secret}")
    private String secretKey;

    public SecretKey generateKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiDate = addHoursToJavaUtilDate(now, 1);
        

        return Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(now)
        .expiration(expiDate)
        .signWith(generateKey(secretKey))
        .compact();
    }

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public void extractTokenData() {}

    public boolean isTokenValid() {
        return false;
    }
}
