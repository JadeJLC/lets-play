# Let's Play – CRUD API

<div align="center">

**API REST de gestion d'utilisateurs et de produits avec authentification JWT, développée avec Spring Boot et MongoDB.**

</div>

## Aperçu

Let's Play est une API REST programmée en Java avec Spring Boot. L'objectif est de fournir un système complet de gestion d'utilisateurs et de produits avec authentification sécurisée, contrôle d'accès basé sur les rôles et protection contre les vulnérabilités courantes.
C'est un projet d'études visant à approfondir les compétences en développement d'API sécurisées, gestion de base de données NoSQL et bonnes pratiques de sécurité web.

## Fonctionnalités

- **Gestion des utilisateurs** : Création, consultation, modification et suppression de comptes.
- **Gestion des produits** : CRUD complet avec rattachement automatique au propriétaire.
- **Authentification JWT** : Connexion sécurisée avec génération de token à durée limitée (1h).
- **Contrôle d'accès basé sur les rôles** : Distinction entre utilisateurs standards et administrateurs.
- **Sécurité renforcée** : Hash des mots de passe (BCrypt), HTTPS, protection contre les injections MongoDB.
- **Gestion centralisée des erreurs** : Réponses HTTP claires pour chaque type d'exception.
- **Rate limiting** : Protection contre le brute-force sur la route de connexion (Bucket4j).
- **CORS configuré** : Prêt pour une intégration avec un frontend.

## Technologies utilisées

**Language & Framework:**

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)

## Modèle de données

User "1" -- "n" Product : Owns
User : +String id
User : +String name
User : +String email
User : +String password
User : +String role
Product : +String id
Product : +String name
Product : +String description
Product : +Double price
Product : +String userId


Un utilisateur (`role` : `user` ou `admin`) peut posséder plusieurs produits. Chaque produit référence son propriétaire via `userId`.

## Utilisation

### Prérequis

- **Java** 17
- **Maven**
- **MongoDB** (installé localement, écoutant sur `localhost:27017`)

> **Note** : Le fichier `application.properties` n'est pas versionné sur ce repository pour des raisons de sécurité (il contient notamment le secret JWT et les informations de connexion à la base). Vous devez le créer vous-même avant de lancer le projet.

### Configuration

Avant de lancer le projet, créez un fichier `application.properties` dans `src/main/resources/` avec, a minima, les propriétés suivantes :

```properties
# Connexion MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/lets-play

# Secret utilisé pour signer les tokens JWT
jwt.secret=votre_secret_jwt_a_definir

# Configuration HTTPS (keystore auto-signé)
server.port=8080
server.ssl.key-store=classpath:keystore/keystore.p12
server.ssl.key-store-password=votre_mot_de_passe_keystore
server.ssl.key-store-type=PKCS12
```

Adaptez les valeurs selon votre environnement (nom de la base, mot de passe du keystore, etc.). Sans ce fichier, l'application ne démarrera pas.

### Installation

1.  **Cloner le repo**

```bash
    git clone https://github.com/JadeJLC/lets-play.git
    cd lets-play
```

2.  **Lancer le projet**

    **Option 1 : script automatique (recommandé)**
    
    Le script démarre MongoDB, attend qu'il soit prêt, lance l'application, puis arrête MongoDB à l'arrêt (`Ctrl+C`).
    
```bash
    chmod +x run.sh
    ./run.sh
```
    
    **Option 2 : lancement manuel**
    
```bash
    sudo systemctl start mongod
    mvn spring-boot:run
```

3.  **Accéder à l'API**
    
    L'application est servie en **HTTPS** uniquement, sur le port `8080` :
https://localhost:8080
    
    Le certificat étant auto-signé, `curl` nécessite l'option `-k` pour ignorer sa vérification.

## Authentification

L'API utilise un système d'authentification par **token JWT** :

1. Créer un compte : `POST /users`
2. Se connecter : `POST /auth/login` (email + mot de passe) → retourne un token
3. Utiliser ce token sur les routes protégées :

Authorization: Bearer <token>


Le token contient l'email et le rôle de l'utilisateur, et expire après 1 heure.

## Routes de l'API

### Utilisateurs (`/users`)

| Méthode | Route         | Accès                           | Description                                       |
| ------- | ------------- | -------------------------------- | -------------------------------------------------- |
| POST    | `/users`      | Public                          | Créer un compte                                    |
| GET     | `/users/{id}` | Authentifié                     | Récupérer un utilisateur (sans mot de passe)       |
| GET     | `/users`      | Admin uniquement                | Lister tous les utilisateurs                       |
| PUT     | `/users`      | Propriétaire ou admin           | Modifier un utilisateur                            |
| DELETE  | `/users/{id}` | Propriétaire ou admin           | Supprimer un utilisateur                           |

### Produits (`/products`)

| Méthode | Route            | Accès                            | Description                              |
| ------- | ---------------- | --------------------------------- | ----------------------------------------- |
| POST    | `/products`      | Authentifié                      | Créer un produit (rattaché au créateur)  |
| GET     | `/products/{id}` | Public                           | Récupérer un produit                     |
| GET     | `/products`      | Public                           | Lister tous les produits                 |
| PUT     | `/products`      | Propriétaire ou admin            | Modifier un produit                      |
| DELETE  | `/products/{id}` | Propriétaire ou admin            | Supprimer un produit                     |

### Authentification (`/auth`)

| Méthode | Route         | Accès  | Description                        |
| ------- | ------------- | ------ | ----------------------------------- |
| POST    | `/auth/login` | Public | Se connecter et récupérer un token  |

## Sécurité

- **Mots de passe hashés** avec BCrypt, jamais stockés ni renvoyés en clair
- **DTO dédié** pour empêcher toute exposition du mot de passe dans les réponses
- **Validation des entrées** contre les injections MongoDB (caractères `$`, `{`, `}`, etc.)
- **Contrôle d'accès basé sur les rôles**, vérifié à la fois par Spring Security et au niveau applicatif
- **Vérifications d'autorisation basées sur les données en base**, jamais sur celles envoyées par le client
- **HTTPS** activé (certificat auto-signé en développement)

## Bonus

- **CORS** : origines `http://localhost:*` autorisées, méthodes `GET/POST/PUT/DELETE`, en-tête `Authorization` accepté
- **Rate limiting** : 5 tentatives de connexion par minute par IP sur `/auth/login` (Bucket4j), réponse `429` au-delà

## Gestion des erreurs

Les exceptions métier sont interceptées globalement (`@ControllerAdvice`) et transformées en réponses HTTP claires :

| Exception                        | Code HTTP |
| --------------------------------- | --------- |
| `UserNotFoundException`          | 404       |
| `ProductNotFoundException`       | 404       |
| `UserAlreadyExistsException`     | 403       |
| `UnauthorizedOperationException` | 403       |
| `AuthenticationException`        | 400       |

## Structure du projet
```
project-root/
├── src/ # Code source de l'application
│   └── main/resources/application.properties  # À créer (non versionné, voir section Configuration)
├── target/ # Fichiers compilés
├── .vscode/ # Configuration de l'éditeur
├── mvnw / mvnw.cmd # Wrapper Maven
├── pom.xml # Configuration Maven et dépendances
├── run.sh # Script de lancement automatique
├── HELP.md # Documentation Spring Boot
└── readme.md # Ce fichier
```


## Apprentissages clés

Ce projet permet de développer des compétences dans :

- Développement d'API REST avec Spring Boot
- Authentification et autorisation via JWT
- Gestion de base de données NoSQL avec MongoDB
- Sécurisation d'une API (HTTPS, hashage, validation des entrées)
- Contrôle d'accès basé sur les rôles
- Gestion centralisée des exceptions
- Protection contre les attaques par force brute (rate limiting)

## Autres informations

- Ce projet est un projet développé dans le cadre de mes études.
- L'objectif était de créer une API sécurisée et complète en suivant les bonnes pratiques du développement backend.


<div align="center">

Par [JadeJLC](https://github.com/JadeJLC)

</div>
