package com.kfokam.todoapi.dto;

import com.kfokam.todoapi.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) pour la création et la mise à jour d'une tâche.
 *
 * <p>Ce DTO sert de contrat entre le client et l'API. Il permet de :</p>
 * <ul>
 *   <li>Valider les données reçues avant traitement</li>
 *   <li>Ne pas exposer directement l'entité JPA</li>
 *   <li>Documenter la structure attendue dans Swagger</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Données requises pour créer ou mettre à jour une tâche")
public class TaskRequestDTO {

    /**
     * Titre de la tâche (obligatoire).
     */
    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 1, max = 100, message = "Le titre doit contenir entre 1 et 100 caractères")
    @Schema(
        description = "Titre de la tâche",
        example = "Faire les courses",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String titre;

    /**
     * Description détaillée de la tâche (optionnelle).
     */
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Schema(
        description = "Description détaillée de la tâche (optionnelle)",
        example = "Acheter du pain, du lait et des œufs"
    )
    private String description;

    /**
     * Statut de la tâche.
     * Si non fourni, la valeur par défaut sera A_FAIRE.
     */
    @Schema(
        description = "Statut de la tâche",
        example = "A_FAIRE",
        allowableValues = {"A_FAIRE", "EN_COURS", "TERMINE"}
    )
    private TaskStatus statut;
}
