package com.project.lets_play;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

// Tests automatisés généré par Claude sur la base du sujet et du code complet du projet

/**
 * Tests d'intégration couvrant les scénarios principaux du sujet :
 * CRUD, authentification, autorisations par rôle, protection contre l'usurpation,
 * et absence d'erreurs 5XX même sur des entrées invalides.
 *
 * Utilise MockMvc : les requêtes passent directement par les Controllers,
 * sans réseau ni TLS, ce qui évite les soucis liés au certificat HTTPS auto-signé.
 *
 * Prérequis : MongoDB doit être démarré (les tests écrivent réellement en base).
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LetsPlayIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Emails uniques à chaque exécution pour éviter les conflits avec des données déjà en base
    private final String suffix = String.valueOf(System.currentTimeMillis());
    private final String userEmail = "user_" + suffix + "@test.com";
    private final String otherEmail = "other_" + suffix + "@test.com";
    private final String adminEmail = "admin_" + suffix + "@test.com";
    private final String password = "motdepasse123";

    private String userId;
    private String userToken;

    private String otherUserId;
    private String otherUserToken;

    private String adminToken;

    /**
     * Avant chaque test : recrée un utilisateur "user", un second utilisateur "other"
     * et un utilisateur "admin", puis récupère leurs tokens.
     * Permet à chaque test de partir d'un état propre et prévisible.
     */
    @BeforeEach
    public void setUp() throws Exception {
        userId = createUser(userEmail, "User One", "user");
        userToken = login(userEmail);

        otherUserId = createUser(otherEmail, "User Two", "user");
        otherUserToken = login(otherEmail);

        createUser(adminEmail, "Admin One", "admin");
        adminToken = login(adminEmail);
    }

    // ---------- Méthodes utilitaires ----------

    private String createUser(String email, String name, String role) throws Exception {
        String body = String.format(
            "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"%s\"}",
            name, email, password, role
        );

        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asText();
    }

    private String login(String email) throws Exception {
        String body = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn();

        return result.getResponse().getContentAsString();
    }

    private String createProduct(String token, String name, double price) throws Exception {
        String body = String.format(
            "{\"name\":\"%s\",\"description\":\"Description de test\",\"price\":%s}",
            name, price
        );

        MvcResult result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asText();
    }

    // ---------- Tests : Utilisateurs ----------

    @Test
    public void creerUnUtilisateur_reussit() throws Exception {
        assertNotNull(userId);
        assertFalse(userId.isEmpty());
    }

    @Test
    public void creerUnUtilisateur_avecEmailDejaExistant_renvoie403() throws Exception {
        String body = String.format(
            "{\"name\":\"Doublon\",\"email\":\"%s\",\"password\":\"%s\",\"role\":\"user\"}",
            userEmail, password
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    public void creerUnUtilisateur_avecCaractereInterdit_renvoie400PasUn500() throws Exception {
        String body = String.format(
            "{\"name\":\"Test\",\"email\":\"injection$%s@test.com\",\"password\":\"%s\",\"role\":\"user\"}",
            suffix, password
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void login_avecMauvaisMotDePasse_renvoie400() throws Exception {
        String body = String.format("{\"email\":\"%s\",\"password\":\"mauvaispassword\"}", userEmail);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void login_avecChampManquant_renvoie400PasUn500() throws Exception {
        String body = String.format("{\"email\":\"%s\"}", userEmail);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getUser_sansToken_estBloque() throws Exception {
        mockMvc.perform(get("/users/" + userId))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getUser_avecToken_neRenvoiePasLeMotDePasse() throws Exception {
        MvcResult result = mockMvc.perform(get("/users/" + userId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.contains("password"), "La réponse ne doit jamais contenir le mot de passe");
    }

    @Test
    public void getUser_avecIdMalforme_neRenvoiePasUn500() throws Exception {
        mockMvc.perform(get("/users/id-invalide-pas-un-objectid")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(result -> assertTrue(
                result.getResponse().getStatus() < 500,
                "Un id malformé ne doit jamais provoquer une erreur 500"
            ));
    }

    @Test
    public void getAllUsers_enTantQueUserNormal_estRefuse() throws Exception {
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsers_enTantQueAdmin_reussit() throws Exception {
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());
    }

    @Test
    public void updateUser_sonPropreCompte_reussit() throws Exception {
        String body = String.format(
            "{\"id\":\"%s\",\"name\":\"Nom Modifié\",\"email\":\"%s\"}",
            userId, userEmail
        );

        mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());
    }

    @Test
    public void updateUser_leCompteDeQuelquunDautre_estRefuse() throws Exception {
        String body = String.format(
            "{\"id\":\"%s\",\"name\":\"Piraté\",\"email\":\"%s\"}",
            otherUserId, otherEmail
        );

        mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + userToken) // token de "user", cible "other"
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    public void updateUser_neLaissePasUnUserSauto_promouvoirAdmin() throws Exception {
        String body = String.format(
            "{\"id\":\"%s\",\"name\":\"User One\",\"email\":\"%s\",\"role\":\"admin\"}",
            userId, userEmail
        );

        mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());

        // Un user normal ne doit plus pouvoir accéder à GET /users (réservé admin)
        // après avoir tenté de s'auto-promouvoir, preuve que le rôle n'a pas changé
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUser_leCompteDeQuelquunDautre_estRefuse() throws Exception {
        mockMvc.perform(delete("/users/" + otherUserId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }

    // ---------- Tests : Produits ----------

    @Test
    public void creerUnProduit_reussit() throws Exception {
        String productId = createProduct(userToken, "Chaise", 49.99);
        assertNotNull(productId);
    }

    @Test
    public void creerUnProduit_estRattacheAuBonProprietaire_pasCeluiEnvoye() throws Exception {
        // On tente d'envoyer un faux userId dans le corps ; il doit être ignoré
        String body = String.format(
            "{\"name\":\"Chaise\",\"description\":\"desc\",\"price\":10.0,\"userId\":\"%s\"}",
            otherUserId
        );

        MvcResult result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertEquals(userId, json.get("userId").asText(),
            "Le produit doit appartenir à l'utilisateur authentifié, pas à celui indiqué dans le corps");
    }

    @Test
    public void getAllProducts_estPublic_sansToken() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().isOk());
    }

    @Test
    public void getProduct_avecIdMalforme_neRenvoiePasUn500() throws Exception {
        mockMvc.perform(get("/products/id-invalide"))
            .andExpect(result -> assertTrue(
                result.getResponse().getStatus() < 500,
                "Un id de produit malformé ne doit jamais provoquer une erreur 500"
            ));
    }

    @Test
    public void updateProduit_dunAutreUtilisateur_estRefuse() throws Exception {
        String productId = createProduct(userToken, "Table", 99.0);

        String body = String.format(
            "{\"id\":\"%s\",\"name\":\"Table piratée\",\"description\":\"desc\",\"price\":1.0}",
            productId
        );

        mockMvc.perform(put("/products")
                .header("Authorization", "Bearer " + otherUserToken) // "other" essaie de modifier le produit de "user"
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    public void updateProduit_falsifierLeUserIdEnvoye_neContournePasLaProtection() throws Exception {
        String productId = createProduct(userToken, "Lampe", 20.0);

        // "other" essaie de modifier le produit de "user" en mentant sur le userId dans le corps
        String body = String.format(
            "{\"id\":\"%s\",\"name\":\"Lampe piratée\",\"description\":\"desc\",\"price\":1.0,\"userId\":\"%s\"}",
            productId, otherUserId
        );

        mockMvc.perform(put("/products")
                .header("Authorization", "Bearer " + otherUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }

    @Test
    public void deleteProduit_parSonProprietaire_reussit() throws Exception {
        String productId = createProduct(userToken, "Vélo", 150.0);

        mockMvc.perform(delete("/products/" + productId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());
    }

    @Test
    public void creerUnProduit_avecPrixNegatif_renvoie400PasUn500() throws Exception {
        String body = "{\"name\":\"Produit\",\"description\":\"desc\",\"price\":-10.0}";

        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }
}
