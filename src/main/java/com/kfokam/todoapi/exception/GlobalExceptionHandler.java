package com.kfokam.todoapi.exception;

import com.kfokam.todoapi.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'ensemble de l'API.
 *
 * <p>{@code @RestControllerAdvice} intercepte toutes les exceptions levées
 * dans les contrôleurs et retourne des réponses HTTP structurées et cohérentes.</p>
 *
 * <p>Exceptions gérées :</p>
 * <ul>
 *   <li>{@link TaskNotFoundException}             → 404 Not Found</li>
 *   <li>{@link MethodArgumentNotValidException}   → 400 Bad Request (erreurs de validation)</li>
 *   <li>{@link IllegalArgumentException}          → 400 Bad Request (argument invalide)</li>
 *   <li>{@link Exception}                         → 500 Internal Server Error (erreur générique)</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Slf4j                      // Lombok : génère un logger SLF4J (log.info, log.error, etc.)
@RestControllerAdvice       // Intercepte les exceptions de tous les contrôleurs REST
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions {@link TaskNotFoundException}.
     * Retourne une réponse 404 quand une tâche n'est pas trouvée.
     *
     * @param ex l'exception levée
     * @return ResponseEntity 404 avec le message d'erreur
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTaskNotFoundException(TaskNotFoundException ex) {
        log.warn("Tâche non trouvée : {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Gère les erreurs de validation des DTOs (annotations @NotBlank, @Size, etc.).
     * Retourne une réponse 400 avec la liste des erreurs par champ.
     *
     * @param ex l'exception de validation levée par Spring
     * @return ResponseEntity 400 avec le détail des erreurs de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // Collecte toutes les erreurs de validation champ par champ
        Map<String, String> erreurs = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erreurs.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Erreurs de validation : {}", erreurs);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Données invalides. Veuillez corriger les erreurs ci-dessous.")
                        .data(erreurs)
                        .build());
    }

    /**
     * Gère les {@link IllegalArgumentException} (arguments invalides).
     * Retourne une réponse 400.
     *
     * @param ex l'exception levée
     * @return ResponseEntity 400 avec le message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Argument invalide : {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Gère toutes les autres exceptions non prévues.
     * Retourne une réponse 500 générique pour ne pas exposer les détails internes.
     *
     * @param ex l'exception non gérée
     * @return ResponseEntity 500 avec un message générique
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Erreur interne inattendue : {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Une erreur interne s'est produite. Veuillez réessayer plus tard."));
    }
}
