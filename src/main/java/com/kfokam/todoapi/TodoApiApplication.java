package com.kfokam.todoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application Todo API.
 *
 * <p>{@code @SpringBootApplication} active :</p>
 * <ul>
 *   <li>{@code @Configuration}     - détecte les beans de configuration</li>
 *   <li>{@code @EnableAutoConfiguration} - configure automatiquement Spring</li>
 *   <li>{@code @ComponentScan}     - scanne les composants du package courant</li>
 * </ul>
 *
 * <p>Une fois lancée, l'API est disponible sur :</p>
 * <ul>
 *   <li>API REST   : <a href="http://localhost:8080/api/v1/tasks">http://localhost:8080/api/v1/tasks</a></li>
 *   <li>Swagger UI : <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></li>
 *   <li>H2 Console : <a href="http://localhost:8080/h2-console">http://localhost:8080/h2-console</a></li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@SpringBootApplication
public class TodoApiApplication {

    /**
     * Méthode principale démarrant le serveur Spring Boot embarqué (Tomcat).
     *
     * @param args arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        SpringApplication.run(TodoApiApplication.class, args);
    }
}
