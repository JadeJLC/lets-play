package com.project.lets_play.config;

import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // Récupération des données du trust.store pour le certificat HTTPS
    @Value("${trust.store}")
    private Resource trustStore;

    @Value("${trust.store.password}")
    private String trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        // Récupère le keystore dans les fichiers (créé par le certificat HTTPS) et évite les fuites mémoires
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream is = trustStore.getInputStream()) {
            keyStore.load(is, trustStorePassword.toCharArray());
        }

        // Vérifie que la requête correspond bien aux données de sécurité du keystore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        // Associe le client HTTP au contexte SSL pour que la sécurité fonctionne correctement
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
            .sslContext(sslContext)
            .build();

        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);

        return new RestTemplate(factory);
    }
}