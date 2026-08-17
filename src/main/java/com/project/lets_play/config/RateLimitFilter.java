package com.project.lets_play.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class RateLimitFilter extends OncePerRequestFilter {
    Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    @Value("${rate-limit.capacity}")
    private int capacity;

    @Value("${rate-limit.refill-minutes}")
    private int refillMinutes;

    private Bucket createNewBucket() {
    return Bucket.builder()
        .addLimit(limit -> limit.capacity(capacity).refillGreedy(capacity, Duration.ofMinutes(refillMinutes)))
        .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String route = request.getRequestURI().toString();
        if (route.contains("/auth/login")) {

            String ip = request.getRemoteAddr();
            Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());
            

            if (bucket.tryConsume(1)) {
        
            } else {
                response.setStatus(429);
                response.getWriter().write("Erreur 429 [Too Many Requests] : Trop de tentatives de connexion. Réessayez plus tard.");
                return;
            }

        }
        
        
        filterChain.doFilter(request, response);
    }
}