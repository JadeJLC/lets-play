# Let's Play – CRUD API (Spring Boot + MongoDB)

API REST de gestion d'utilisateurs et de produits, développée avec Spring Boot et MongoDB. Le projet implémente l'authentification par token (JWT), le contrôle d'accès basé sur les rôles, la gestion centralisée des erreurs et plusieurs mesures de sécurité (hash des mots de passe, HTTPS, protection contre les injections MongoDB).

## Sommaire

- [Modèle de données](#modèle-de-données)
- [Prérequis](#prérequis)
- [Lancer le projet](#lancer-le-projet)
- [Authentification](#authentification)
- [Routes de l'API](#routes-de-lapi)
- [Sécurité](#sécurité)
- [Bonus](#bonus)
- [Gestion des erreurs](#gestion-des-erreurs)

## Modèle de données

```
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
```

Un utilisateur (`role`: `user` ou `admin`) peut posséder plusieurs produits. Chaque produit référence son propriétaire via `userId`.

## Prérequis

- Java 17
- Maven
- MongoDB (installé localement, écoutant sur `localhost:27017`)

## Lancer le projet

### Option 1 : script automatique (recommandé)

Un script `run.sh` est fourni à la racine du projet. Il démarre MongoDB, attend qu'il soit prêt, puis lance l'application. Il arrête aussi MongoDB automatiquement lorsque l'application est interrompue (`Ctrl+C`).

```bash
chmod +x run.sh
./run.sh
```

Le script demandera votre mot de passe administrateur (`sudo`) pour démarrer/arrêter le service MongoDB.

### Option 2 : lancement manuel

```bash
sudo systemctl start mongod
mvn spring-boot:run
```

### Accès à l'API

L'application est servie en **HTTPS** uniquement, sur le port `8080` :

```
https://localhost:8080
```

Le certificat étant auto-signé (usage de développement), les outils comme `curl` nécessitent une option pour ignorer la vérification du certificat :

```bash
curl -k https://localhost:8080/products
```

## Authentification

L'API utilise un système d'authentification par **token JWT**.

1. Créer un compte : `POST /users`
2. Se connecter : `POST /auth/login` avec l'email et le mot de passe → retourne un token
3. Utiliser ce token sur les routes protégées, dans l'en-tête HTTP :

```
Authorization: Bearer <token>
```

Le token contient l'email et le rôle de l'utilisateur, et expire après 1 heure.

## Routes de l'API

### Utilisateurs (`/users`)

| Méthode | Route         | Accès                           | Description                                                              |
| ------- | ------------- | ------------------------------- | ------------------------------------------------------------------------ |
| POST    | `/users`      | Public                          | Créer un compte                                                          |
| GET     | `/users/{id}` | Authentifié                     | Récupérer un utilisateur (sans son mot de passe)                         |
| GET     | `/users`      | Admin uniquement                | Lister tous les utilisateurs                                             |
| PUT     | `/users`      | Propriétaire du compte ou admin | Modifier un utilisateur (le changement de `role` est réservé à un admin) |
| DELETE  | `/users/{id}` | Propriétaire du compte ou admin | Supprimer un utilisateur                                                 |

### Produits (`/products`)

| Méthode | Route            | Accès                            | Description                                                          |
| ------- | ---------------- | -------------------------------- | -------------------------------------------------------------------- |
| POST    | `/products`      | Authentifié                      | Créer un produit (rattaché automatiquement à l'utilisateur connecté) |
| GET     | `/products/{id}` | Public                           | Récupérer un produit                                                 |
| GET     | `/products`      | Public                           | Lister tous les produits                                             |
| PUT     | `/products`      | Propriétaire du produit ou admin | Modifier un produit                                                  |
| DELETE  | `/products/{id}` | Propriétaire du produit ou admin | Supprimer un produit                                                 |

### Authentification (`/auth`)

| Méthode | Route         | Accès  | Description                        |
| ------- | ------------- | ------ | ---------------------------------- |
| POST    | `/auth/login` | Public | Se connecter et récupérer un token |

## Exemples d'utilisation

Les exemples ci-dessous utilisent `curl`. L'option `-k` est nécessaire car le certificat HTTPS est auto-signé.

### 1. Créer un compte utilisateur

```bash
curl -k -X POST https://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Marie Test","email":"marie@test.com","password":"motdepasse123","role":"user"}'
```

Réponse : l'utilisateur créé (sans son mot de passe).

### 2. Se connecter et récupérer un token

```bash
curl -k -X POST https://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"marie@test.com","password":"motdepasse123"}'
```

Réponse : une chaîne correspondant au token JWT, par exemple :

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJpZUB0ZXN0LmNvbSIs...
```

Conservez ce token, il est nécessaire pour toutes les routes protégées ci-dessous (remplacez `TOKEN` par sa valeur).

### 3. Récupérer son propre profil

```bash
curl -k -X GET https://localhost:8080/users/<id_utilisateur> \
  -H "Authorization: Bearer TOKEN"
```

### 4. Modifier son profil (nom, email)

```bash
curl -k -X PUT https://localhost:8080/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"id":"<id_utilisateur>","name":"Marie D.","email":"marie@test.com"}'
```

Le mot de passe existant est conservé automatiquement ; il n'est pas modifiable via cette route. Le champ `role` n'est pris en compte que si la requête est faite par un administrateur.

### 5. Créer un produit

Le produit est automatiquement rattaché à l'utilisateur authentifié (le champ `userId` envoyé, le cas échéant, est ignoré).

```bash
curl -k -X POST https://localhost:8080/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"name":"Chaise","description":"Une belle chaise en bois","price":49.99}'
```

### 6. Lister tous les produits (public, pas de token requis)

```bash
curl -k -X GET https://localhost:8080/products
```

### 7. Modifier un produit

Seul le propriétaire du produit (ou un administrateur) peut le modifier.

```bash
curl -k -X PUT https://localhost:8080/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"id":"<id_produit>","name":"Chaise (soldée)","description":"Une belle chaise en bois","price":39.99}'
```

### 8. Supprimer un produit

```bash
curl -k -X DELETE https://localhost:8080/products/<id_produit> \
  -H "Authorization: Bearer TOKEN"
```

### 9. Lister tous les utilisateurs (admin uniquement)

```bash
curl -k -X GET https://localhost:8080/users \
  -H "Authorization: Bearer TOKEN_ADMIN"
```

Retourne un `403` si le token utilisé n'appartient pas à un compte `admin`.

### 10. Supprimer un utilisateur

```bash
curl -k -X DELETE https://localhost:8080/users/<id_utilisateur> \
  -H "Authorization: Bearer TOKEN"
```

## Sécurité

- **Mots de passe hashés** avec BCrypt (via `PasswordEncoder`), jamais stockés ni renvoyés en clair
- **Mots de passe jamais exposés** dans les réponses de l'API (utilisation d'un DTO `UserResponse` dédié)
- **Validation des entrées** (`InputValidation`) pour bloquer les caractères utilisés dans les injections MongoDB (`$`, `{`, `}`, `[`, `]`, `"`, `'`, `\`) sur les champs texte, et vérification des valeurs numériques (prix non négatif)
- **Contrôle d'accès basé sur les rôles**, vérifié à la fois au niveau de Spring Security (`SecurityConfig`) et au niveau applicatif (vérification de propriété sur les ressources utilisateur/produit)
- **Protection contre l'usurpation de propriété** : les vérifications d'autorisation se basent toujours sur les données existantes en base, jamais sur celles envoyées par le client
- **HTTPS** activé (certificat auto-signé de développement, voir `src/main/resources/keystore/`)

## Bonus

### CORS

Une politique CORS est configurée dans `SecurityConfig` :

- Origines autorisées : `http://localhost:*` (tout port local, pratique pour un futur frontend en développement)
- Méthodes autorisées : `GET`, `POST`, `PUT`, `DELETE`
- En-têtes autorisés : tous (`*`), pour permettre notamment l'en-tête `Authorization` utilisé par le token JWT

### Rate limiting

La route `POST /auth/login` est protégée contre les attaques par force brute via `RateLimitFilter` (bibliothèque Bucket4j) :

- Limite : **5 tentatives de connexion par minute**, par adresse IP
- Au-delà de cette limite, l'API répond `429 Too Many Requests` jusqu'à ce que le quota se recharge

Les autres routes de l'API ne sont pas concernées par cette limite.

## Gestion des erreurs

Toutes les exceptions métier sont interceptées globalement par `GlobalExceptionHandler` (`@ControllerAdvice`) et transformées en réponses HTTP claires, sans jamais renvoyer d'erreur 5XX :

| Exception                        | Code HTTP |
| -------------------------------- | --------- |
| `UserNotFoundException`          | 404       |
| `ProductNotFoundException`       | 404       |
| `UserAlreadyExistsException`     | 403       |
| `UnauthorizedOperationException` | 403       |
| `AuthenticationException`        | 400       |
