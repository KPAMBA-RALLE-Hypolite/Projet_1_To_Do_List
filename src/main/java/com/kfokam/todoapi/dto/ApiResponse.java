package com.kfokam.todoapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Wrapper générique pour standardiser toutes les réponses de l'API.
 *
 * <p>Toutes les réponses de l'API suivent cette structure uniforme :</p>
 * <pre>
 * {
 *   "success": true,
 *   "message": "Tâche créée avec succès",
 *   "data": { ... },
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * </pre>
 *
 * <p>En cas d'erreur :</p>
 * <pre>
 * {
 *   "success": false,
 *   "message": "Tâche non trouvée avec l'ID : 99",
 *   "data": null,
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * </pre>
 *
 * @param <T> le type de données encapsulé dans la réponse
 * @author KFOKAM48
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclut les champs null du JSON
@Schema(description = "Structure standard de toutes les réponses API")
public class ApiResponse<T> {

    /**
     * Indique si l'opération a réussi.
     */
    @Schema(description = "Succès de l'opération", example = "true")
    private boolean success;

    /**
     * Message descriptif du résultat.
     */
    @Schema(description = "Message de résultat", example = "Tâche créée avec succès")
    private String message;

    /**
     * Les données retournées (null en cas d'erreur ou de suppression).
     */
    @Schema(description = "Données retournées")
    private T data;

    /**
     * Horodatage de la réponse.
     */
    @Schema(description = "Horodatage de la réponse")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // ===== Méthodes utilitaires statiques =====

    /**
     * Crée une réponse de succès avec données.
     *
     * @param message le message de succès
     * @param data    les données à retourner
     * @param <T>     le type des données
     * @return une réponse API de succès
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crée une réponse de succès sans données (ex: après suppression).
     *
     * @param message le message de succès
     * @param <T>     le type des données
     * @return une réponse API de succès sans données
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crée une réponse d'erreur.
     *
     * @param message le message d'erreur
     * @param <T>     le type des données
     * @return une réponse API d'erreur
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
