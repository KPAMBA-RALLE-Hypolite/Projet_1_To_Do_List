package com.kfokam.todoapi.dto;

import com.kfokam.todoapi.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) représentant la réponse API pour une tâche.
 *
 * <p>Ce DTO est retourné au client après chaque opération réussie.
 * Il contient toutes les informations de la tâche, y compris les
 * métadonnées (dates de création et de mise à jour).</p>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représentation d'une tâche retournée par l'API")
public class TaskResponseDTO {

    /**
     * Identifiant unique de la tâche.
     */
    @Schema(description = "Identifiant unique de la tâche", example = "1")
    private Long id;

    /**
     * Titre de la tâche.
     */
    @Schema(description = "Titre de la tâche", example = "Faire les courses")
    private String titre;

    /**
     * Description de la tâche.
     */
    @Schema(description = "Description de la tâche", example = "Acheter du pain, du lait et des œufs")
    private String description;

    /**
     * Statut actuel de la tâche.
     */
    @Schema(description = "Statut de la tâche", example = "A_FAIRE")
    private TaskStatus statut;

    /**
     * Date et heure de création de la tâche.
     */
    @Schema(description = "Date de création", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    /**
     * Date et heure de la dernière mise à jour.
     */
    @Schema(description = "Date de dernière modification", example = "2024-01-15T14:00:00")
    private LocalDateTime updatedAt;
}
