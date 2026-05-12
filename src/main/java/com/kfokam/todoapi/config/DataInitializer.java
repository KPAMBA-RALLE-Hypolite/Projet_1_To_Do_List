package com.kfokam.todoapi.config;

import com.kfokam.todoapi.model.Task;
import com.kfokam.todoapi.model.TaskStatus;
import com.kfokam.todoapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Initialisation de données de test au démarrage de l'application.
 *
 * <p>Ce composant injecte quelques tâches exemples dans la base de données
 * H2 au démarrage. Cela permet de tester l'API immédiatement sans avoir
 * à créer manuellement des données.</p>
 *
 * <p><strong>Note :</strong> Ce bean ne s'exécute que si la BDD est vide.
 * À désactiver en production.</p>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TaskRepository taskRepository;

    /**
     * Insère des tâches d'exemple si la base de données est vide.
     *
     * @return un CommandLineRunner exécuté au démarrage Spring Boot
     */
    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // N'insérer les données que si la BDD est vide
            if (taskRepository.count() == 0) {
                taskRepository.save(Task.builder()
                        .titre("Configurer l'environnement de développement")
                        .description("Installer Java 17, Maven, IntelliJ IDEA et configurer le projet Spring Boot")
                        .statut(TaskStatus.TERMINE)
                        .build());

                taskRepository.save(Task.builder()
                        .titre("Développer les endpoints REST")
                        .description("Implémenter les opérations CRUD : POST, GET, PUT, DELETE")
                        .statut(TaskStatus.EN_COURS)
                        .build());

                taskRepository.save(Task.builder()
                        .titre("Rédiger la documentation Swagger")
                        .description("Documenter tous les endpoints avec des descriptions et exemples clairs")
                        .statut(TaskStatus.EN_COURS)
                        .build());

                taskRepository.save(Task.builder()
                        .titre("Écrire les tests unitaires")
                        .description("Couvrir les cas nominaux et les cas d'erreur du service et du contrôleur")
                        .statut(TaskStatus.A_FAIRE)
                        .build());

                taskRepository.save(Task.builder()
                        .titre("Déployer sur le serveur de production")
                        .description("Packager le JAR et déployer sur le serveur avec Docker ou directement")
                        .statut(TaskStatus.A_FAIRE)
                        .build());
            }
        };
    }
}
