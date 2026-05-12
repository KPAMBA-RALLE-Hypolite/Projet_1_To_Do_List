package com.kfokam.todoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration de la documentation OpenAPI (Swagger).
 *
 * <p>Cette classe personnalise l'interface Swagger UI avec :</p>
 * <ul>
 *   <li>Les informations du projet (titre, description, version)</li>
 *   <li>Les coordonnées de l'auteur/équipe</li>
 *   <li>La licence du projet</li>
 *   <li>Les environnements disponibles (local, production)</li>
 * </ul>
 *
 * <p>Swagger UI accessible à : <a href="http://localhost:8080/swagger-ui.html">
 * http://localhost:8080/swagger-ui.html</a></p>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Configuration  // Indique que cette classe contient des configurations Spring
public class SwaggerConfig {

    /**
     * Configure et personnalise la documentation OpenAPI.
     *
     * @return l'objet OpenAPI configuré
     */
    @Bean
    public OpenAPI customOpenAPI() {

        // Serveur local (développement)
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Serveur de développement local");

        // Informations de contact
        Contact contact = new Contact();
        contact.setName("KFOKAM48");
        contact.setEmail("kpambahypolite1@gmail.com");
        contact.setUrl("https://github.com/kfokam48");

        // Licence du projet
        License licence = new License();
        licence.setName("MIT License");
        licence.setUrl("https://opensource.org/licenses/MIT");

        // Informations générales de l'API
        Info info = new Info()
                .title("API de Gestion de Tâches — To-Do List")
                .version("1.0.0")
                .description("""
                        ## Description
                        API REST développée avec **Spring Boot** permettant de gérer une liste de tâches.
                        ## Fonctionnalités
                        - ✅ **Créer** une tâche avec titre, description et statut
                        - 📋 **Lire** toutes les tâches avec filtrage par statut
                        - ✏️ **Mettre à jour** le titre, la description ou le statut
                        - 🗑️ **Supprimer** une tâche
                        ## Statuts possibles
                        | Statut    | Description               |
                        |-----------|---------------------------|
                        | `A_FAIRE`  | Tâche créée, pas commencée |
                        | `EN_COURS` | Tâche en cours d'exécution |
                        | `TERMINE`  | Tâche entièrement terminée |
                        """)
                .contact(contact)
                .license(licence);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
