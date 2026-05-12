# 📋 API de Gestion de Tâches (To-Do List)

> Projet 1 — Java Spring Boot | KFOKAM48

API REST complète permettant de gérer une liste de tâches avec les opérations CRUD.

---

## 📌 Table des matières

- [Stack technologique](#stack-technologique)
- [Prérequis](#prérequis)
- [Installation et démarrage](#installation-et-démarrage)
- [Endpoints de l'API](#endpoints-de-lapi)
- [Exemples de requêtes](#exemples-de-requêtes)
- [Structure du projet](#structure-du-projet)
- [Documentation Swagger](#documentation-swagger)
- [Base de données H2](#base-de-données-h2)

---

## 🛠️ Stack technologique

| Technologie        | Version | Rôle                                    |
|--------------------|---------|------------------------------------------|
| Java               | 17      | Langage de programmation                 |
| Spring Boot        | 3.2.0   | Framework principal                      |
| Spring Data JPA    | 3.2.0   | Persistance des données (ORM)            |
| Lombok             | latest  | Réduction du code boilerplate            |
| SpringDoc OpenAPI  | 2.3.0   | Documentation Swagger automatique        |
| H2 Database        | latest  | Base de données en mémoire (dev)         |
| Maven              | 3.9+    | Outil de build et gestion des dépendances|
| JUnit 5 + Mockito  | latest  | Tests unitaires                          |

---

## ✅ Prérequis

Avant de lancer l'application, assurez-vous d'avoir installé :

- **Java 17+** → [Télécharger](https://adoptium.net/)
- **Maven 3.9+** → [Télécharger](https://maven.apache.org/download.cgi)

Vérifier les versions :
```bash
java -version
# java version "17.x.x" ...

mvn -version
# Apache Maven 3.9.x ...
```

---

## 🚀 Installation et démarrage

### 1. Cloner le dépôt
```bash
git clone https://github.com/kfokam48/todo-api.git
cd todo-api
```

### 2. Compiler le projet
```bash
mvn clean install
```

### 3. Lancer l'application
```bash
mvn spring-boot:run
```

Ou via le JAR généré :
```bash
java -jar target/todo-api-1.0.0.jar
```

### 4. Vérifier que l'API est démarrée
```
✅ Tomcat started on port(s): 8080
✅ Started TodoApiApplication
```

L'API est disponible sur **http://localhost:8080**

---

## 🔗 Endpoints de l'API

Base URL : `http://localhost:8080/api/v1`

| Méthode  | Endpoint              | Description                            |
|----------|-----------------------|----------------------------------------|
| `POST`   | `/tasks`              | Créer une nouvelle tâche               |
| `GET`    | `/tasks`              | Lister toutes les tâches               |
| `GET`    | `/tasks?statut=X`     | Filtrer les tâches par statut          |
| `GET`    | `/tasks/{id}`         | Récupérer une tâche par ID             |
| `PUT`    | `/tasks/{id}`         | Mettre à jour une tâche                |
| `DELETE` | `/tasks/{id}`         | Supprimer une tâche                    |

### Statuts disponibles

| Valeur     | Signification              |
|------------|---------------------------|
| `A_FAIRE`  | Tâche pas encore commencée |
| `EN_COURS` | Tâche en cours d'exécution |
| `TERMINE`  | Tâche terminée             |

---

## 📡 Exemples de requêtes

### Créer une tâche
```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Faire les courses",
    "description": "Acheter du pain, du lait et des oeufs",
    "statut": "A_FAIRE"
  }'
```

**Réponse (201 Created) :**
```json
{
  "success": true,
  "message": "Tâche créée avec succès",
  "data": {
    "id": 1,
    "titre": "Faire les courses",
    "description": "Acheter du pain, du lait et des oeufs",
    "statut": "A_FAIRE",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Lister toutes les tâches
```bash
curl http://localhost:8080/api/v1/tasks
```

### Filtrer par statut
```bash
curl "http://localhost:8080/api/v1/tasks?statut=A_FAIRE"
```

### Récupérer une tâche par ID
```bash
curl http://localhost:8080/api/v1/tasks/1
```

### Mettre à jour une tâche
```bash
curl -X PUT http://localhost:8080/api/v1/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Faire les courses (urgent)",
    "statut": "EN_COURS"
  }'
```

### Supprimer une tâche
```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/1
```

---

## 📚 Documentation Swagger

L'interface Swagger UI est automatiquement générée et accessible à :

🔗 **http://localhost:8080/swagger-ui.html**

La spécification OpenAPI (JSON) est disponible à :

🔗 **http://localhost:8080/api-docs**

---

## 🗄️ Base de données H2

En développement, l'application utilise une base de données **H2 en mémoire**.
La console web H2 est accessible à :

🔗 **http://localhost:8080/h2-console**

| Paramètre      | Valeur                              |
|----------------|-------------------------------------|
| JDBC URL       | `jdbc:h2:mem:tododb`                |
| Nom d'utilisateur | `sa`                             |
| Mot de passe   | *(laisser vide)*                    |

> ⚠️ Les données sont **perdues** à chaque redémarrage (base en mémoire).
> Des données de démonstration sont automatiquement insérées au démarrage.

---

## 🧪 Lancer les tests

```bash
mvn test
```

Les tests couvrent :
- Création de tâche (cas nominal et valeur par défaut)
- Lecture avec et sans filtre
- Mise à jour (cas nominal et ID inexistant)
- Suppression (cas nominal et ID inexistant)

---

## 📁 Structure du projet

```
todo-api/
├── src/
│   ├── main/
│   │   ├── java/com/kfokam/todoapi/
│   │   │   ├── TodoApiApplication.java     ← Point d'entrée Spring Boot
│   │   │   ├── config/
│   │   │   │   ├── SwaggerConfig.java      ← Configuration OpenAPI
│   │   │   │   └── DataInitializer.java    ← Données de démonstration
│   │   │   ├── controller/
│   │   │   │   └── TaskController.java     ← Endpoints REST
│   │   │   ├── service/
│   │   │   │   └── TaskService.java        ← Logique métier
│   │   │   ├── repository/
│   │   │   │   └── TaskRepository.java     ← Accès base de données
│   │   │   ├── model/
│   │   │   │   ├── Task.java               ← Entité JPA
│   │   │   │   └── TaskStatus.java         ← Enum des statuts
│   │   │   ├── dto/
│   │   │   │   ├── TaskRequestDTO.java     ← DTO entrée (requêtes)
│   │   │   │   ├── TaskResponseDTO.java    ← DTO sortie (réponses)
│   │   │   │   └── ApiResponse.java        ← Wrapper générique de réponse
│   │   │   └── exception/
│   │   │       ├── TaskNotFoundException.java       ← Exception métier
│   │   │       └── GlobalExceptionHandler.java      ← Gestionnaire global
│   │   └── resources/
│   │       └── application.properties      ← Configuration
│   └── test/
│       └── java/com/kfokam/todoapi/
│           └── TaskServiceTest.java        ← Tests unitaires
├── pom.xml                                 ← Dépendances Maven
└── README.md                               ← Ce fichier
```

---

## 👤 Auteur

**KFOKAM48** — Projet de formation Java Spring Boot

---

*Développé avec ☕ Java et 🍃 Spring Boot*