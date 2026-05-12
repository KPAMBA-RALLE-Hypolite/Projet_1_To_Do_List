package com.kfokam.todoapi.repository;

import com.kfokam.todoapi.model.Task;
import com.kfokam.todoapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository JPA pour la gestion des tâches en base de données.
 *
 * <p>Étend {@link JpaRepository} qui fournit automatiquement les opérations CRUD de base :</p>
 * <ul>
 *   <li>{@code save(Task)}          - Créer ou mettre à jour une tâche</li>
 *   <li>{@code findById(Long)}      - Trouver une tâche par son ID</li>
 *   <li>{@code findAll()}           - Récupérer toutes les tâches</li>
 *   <li>{@code deleteById(Long)}    - Supprimer une tâche par son ID</li>
 *   <li>{@code count()}             - Compter le nombre de tâches</li>
 * </ul>
 *
 * <p>Spring Data JPA génère automatiquement l'implémentation SQL
 * à partir du nom des méthodes (Query Derivation).</p>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Récupère toutes les tâches ayant un statut donné.
     *
     * <p>Spring Data JPA traduit automatiquement cette méthode en :
     * {@code SELECT * FROM tasks WHERE statut = ?}</p>
     *
     * @param statut le statut à filtrer (A_FAIRE, EN_COURS, TERMINE)
     * @return la liste des tâches correspondant au statut donné
     */
    List<Task> findByStatut(TaskStatus statut);

    /**
     * Vérifie si une tâche existe avec un titre donné.
     *
     * <p>Utile pour éviter les doublons (titre insensible à la casse).</p>
     *
     * @param titre le titre à vérifier
     * @return true si une tâche avec ce titre existe déjà
     */
    boolean existsByTitreIgnoreCase(String titre);
}
