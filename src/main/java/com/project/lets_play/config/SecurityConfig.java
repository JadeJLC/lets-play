package com.project.lets_play.config;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Gestion des mots de passe et de la sécurité de l'API
 */
@Configuration
public class SecurityConfig {
	private final AuthFilter authFilter;

	public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

	/**
	 * Gestion des mots de passe via PasswordEncoder, fourni par SpringBoot
	 * Encode les mots de passe selon la méthode moderne la plus sécurisée, sans casser les précédents mots de passe
	 */
    @Bean
    public PasswordEncoder createPasswordEncoder() {
		PasswordEncoder passwordEncoder =
		PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder;
	}

	/**
	 * Gestion des autorisations d'accès à l'API via analyse des requêtes HTML
	 * Statut temporaire sur permitAll pour les tests internes, sera modifié ultérieurement
	 * @param http {HttpSecurity} élément SpringSecurity lié à la requête
	 * @return {FilterChain} une série d'actions de vérification à appliquer
	 * @throws Exception 
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
    	    .csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.GET, "/products").permitAll()
				.requestMatchers(HttpMethod.GET, "/users").hasAuthority("admin")
            	.anyRequest().authenticated()
        	)
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Vérifie le token avant que SpringSecurity cherche un formulaire de connexion

    	return http.build();
	}

	
}
