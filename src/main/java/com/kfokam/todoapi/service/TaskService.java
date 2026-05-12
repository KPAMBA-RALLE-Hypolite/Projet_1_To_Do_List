package com.kfokam.todoapi.service;

import com.kfokam.todoapi.dto.TaskRequestDTO;
import com.kfokam.todoapi.dto.TaskResponseDTO;
import com.kfokam.todoapi.exception.TaskNotFoundException;
import com.kfokam.todoapi.model.Task;
import com.kfokam.todoapi.model.TaskStatus;
import com.kfokam.todoapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service contenant toute la logique métier de gestion des tâches.
 *
 * <p>Cette couche intermédiaire entre le contrôleur et le repository :</p>
 * <ul>
 *   <li>Contient la logique métier (validation, transformation, règles)</li>
 *   <li>Convertit les entités JPA en DTOs (et inversement)</li>
 *   <li>Gère les transactions avec {@code @Transactional}</li>
 *   <li>Lève les exceptions métier appropriées</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Slf4j
@Service                    // Marque cette classe comme un composant Service Spring
@RequiredArgsConstructor    // Lombok : génère un constructeur pour l'injection de dépendances
public class TaskService {

    // Injection de dépendance par constructeur (recommandée par Spring)
    private final TaskRepository taskRepository;

    // ===========================
    //  CRÉER UNE TÂCHE
    // ===========================

    /**
     * Crée une nouvelle tâche en base de données.
     *
     * <p>Règles métier :</p>
     * <ul>
     *   <li>Le statut par défaut est {@link TaskStatus#A_FAIRE} si non fourni</li>
     * </ul>
     *
     * @param dto les données de la tâche à créer
     * @return le DTO de la tâche créée (avec son ID généré)
     */
    @Transactional
    public TaskResponseDTO creerTache(TaskRequestDTO dto) {
        log.info("Création d'une nouvelle tâche : '{}'", dto.getTitre());

        // Construire l'entité Task depuis le DTO
        Task task = Task.builder()
                .titre(dto.getTitre().trim())
                .description(dto.getDescription() != null ? dto.getDescription().trim() : null)
                // Si le statut n'est pas fourni, on utilise A_FAIRE par défaut
                .statut(dto.getStatut() != null ? dto.getStatut() : TaskStatus.A_FAIRE)
                .build();

        // Sauvegarder en base et retourner le DTO
        Task savedTask = taskRepository.save(task);
        log.info("Tâche créée avec succès. ID : {}", savedTask.getId());

        return convertirEnDTO(savedTask);
    }

    // ===========================
    //  LIRE LES TÂCHES
    // ===========================

    /**
     * Récupère toutes les tâches, avec filtrage optionnel par statut.
     *
     * @param statut filtre optionnel par statut (null = toutes les tâches)
     * @return la liste des tâches (filtrée ou complète)
     */
    @Transactional(readOnly = true)  // readOnly améliore les performances en lecture
    public List<TaskResponseDTO> getToutesTaches(TaskStatus statut) {
        List<Task> tasks;

        if (statut != null) {
            // Filtrage par statut demandé
            log.info("Récupération des tâches avec statut : {}", statut);
            tasks = taskRepository.findByStatut(statut);
        } else {
            // Retourner toutes les tâches
            log.info("Récupération de toutes les tâches");
            tasks = taskRepository.findAll();
        }

        log.info("{} tâche(s) trouvée(s)", tasks.size());

        // Convertir chaque entité en DTO
        return tasks.stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une tâche spécifique par son identifiant.
     *
     * @param id l'identifiant de la tâche
     * @return le DTO de la tâche trouvée
     * @throws TaskNotFoundException si aucune tâche n'existe avec cet ID
     */
    @Transactional(readOnly = true)
    public TaskResponseDTO getTacheParId(Long id) {
        log.info("Récupération de la tâche ID : {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return convertirEnDTO(task);
    }

    // ===========================
    //  METTRE À JOUR UNE TÂCHE
    // ===========================

    /**
     * Met à jour une tâche existante.
     *
     * <p>Seuls les champs non-null du DTO sont mis à jour (mise à jour partielle).</p>
     *
     * @param id  l'identifiant de la tâche à modifier
     * @param dto les nouvelles données
     * @return le DTO de la tâche mise à jour
     * @throws TaskNotFoundException si la tâche n'existe pas
     */
    @Transactional
    public TaskResponseDTO mettreAJourTache(Long id, TaskRequestDTO dto) {
        log.info("Mise à jour de la tâche ID : {}", id);

        // Vérifier que la tâche existe
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Mettre à jour seulement les champs fournis (non null)
        if (dto.getTitre() != null && !dto.getTitre().isBlank()) {
            task.setTitre(dto.getTitre().trim());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription().trim());
        }
        if (dto.getStatut() != null) {
            task.setStatut(dto.getStatut());
        }

        // JPA détecte les changements automatiquement (@Transactional)
        Task updatedTask = taskRepository.save(task);
        log.info("Tâche ID {} mise à jour avec succès", id);

        return convertirEnDTO(updatedTask);
    }

    // ===========================
    //  SUPPRIMER UNE TÂCHE
    // ===========================

    /**
     * Supprime définitivement une tâche par son identifiant.
     *
     * @param id l'identifiant de la tâche à supprimer
     * @throws TaskNotFoundException si la tâche n'existe pas
     */
    @Transactional
    public void supprimerTache(Long id) {
        log.info("Suppression de la tâche ID : {}", id);

        // Vérifier que la tâche existe avant de supprimer
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
        log.info("Tâche ID {} supprimée avec succès", id);
    }

    // ===========================
    //  MÉTHODE UTILITAIRE PRIVÉE
    // ===========================

    /**
     * Convertit une entité {@link Task} en {@link TaskResponseDTO}.
     *
     * <p>Cette méthode centralisée assure une conversion cohérente
     * dans toutes les méthodes du service.</p>
     *
     * @param task l'entité à convertir
     * @return le DTO correspondant
     */
    private TaskResponseDTO convertirEnDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .titre(task.getTitre())
                .description(task.getDescription())
                .statut(task.getStatut())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
