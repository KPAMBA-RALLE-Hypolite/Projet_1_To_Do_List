package com.kfokam.todoapi.controller;

import com.kfokam.todoapi.dto.ApiResponse;
import com.kfokam.todoapi.dto.TaskRequestDTO;
import com.kfokam.todoapi.dto.TaskResponseDTO;
import com.kfokam.todoapi.model.TaskStatus;
import com.kfokam.todoapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST exposant les endpoints de gestion des tâches.
 *
 * <p>Base URL : {@code /api/v1/tasks}</p>
 *
 * <p>Endpoints disponibles :</p>
 * <ul>
 *   <li>{@code POST   /api/v1/tasks}        - Créer une nouvelle tâche</li>
 *   <li>{@code GET    /api/v1/tasks}         - Lister toutes les tâches (avec filtre optionnel)</li>
 *   <li>{@code GET    /api/v1/tasks/{id}}    - Récupérer une tâche par ID</li>
 *   <li>{@code PUT    /api/v1/tasks/{id}}    - Mettre à jour une tâche</li>
 *   <li>{@code DELETE /api/v1/tasks/{id}}    - Supprimer une tâche</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Slf4j
@RestController                         // Marque la classe comme contrôleur REST (retourne du JSON)
@RequestMapping("/api/v1/tasks")        // Préfixe de tous les endpoints de ce contrôleur
@RequiredArgsConstructor                // Lombok : injection par constructeur
@Tag(name = "Gestion des Tâches",      // Documentation Swagger : nom du groupe
     description = "Opérations CRUD pour la gestion des tâches (To-Do List)")
public class TaskController {

    private final TaskService taskService;

    // ============================================================
    //  POST /api/v1/tasks — Créer une nouvelle tâche
    // ============================================================

    /**
     * Crée une nouvelle tâche.
     *
     * @param dto les données de la tâche à créer (validées automatiquement)
     * @return 201 Created avec la tâche créée
     */
    @PostMapping
    @Operation(
        summary     = "Créer une tâche",
        description = "Crée une nouvelle tâche avec un titre, une description optionnelle et un statut."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description  = "Tâche créée avec succès",
            content      = @Content(schema = @Schema(implementation = TaskResponseDTO.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description  = "Données invalides (titre manquant, trop long, etc.)"
        )
    })
    public ResponseEntity<ApiResponse<TaskResponseDTO>> creerTache(
            @Valid @RequestBody TaskRequestDTO dto) {

        log.info("POST /api/v1/tasks — Création d'une tâche : '{}'", dto.getTitre());
        TaskResponseDTO tache = taskService.creerTache(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201 Created
                .body(ApiResponse.success("Tâche créée avec succès", tache));
    }

    // ============================================================
    //  GET /api/v1/tasks — Lister toutes les tâches
    // ============================================================

    /**
     * Récupère toutes les tâches, avec filtrage optionnel par statut.
     *
     * @param statut filtre optionnel (A_FAIRE, EN_COURS, TERMINE)
     * @return 200 OK avec la liste des tâches
     */
    @GetMapping
    @Operation(
        summary     = "Lister les tâches",
        description = "Récupère toutes les tâches. Vous pouvez filtrer par statut avec le paramètre ?statut="
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description  = "Liste des tâches retournée avec succès"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description  = "Statut de filtre invalide"
        )
    })
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getToutesTaches(
            @Parameter(description = "Filtrer par statut (optionnel)",
                       example     = "A_FAIRE")
            @RequestParam(required = false) TaskStatus statut) {

        log.info("GET /api/v1/tasks — Filtre statut : {}", statut);
        List<TaskResponseDTO> taches = taskService.getToutesTaches(statut);

        String message = statut != null
                ? taches.size() + " tâche(s) avec le statut " + statut
                : taches.size() + " tâche(s) au total";

        return ResponseEntity.ok(ApiResponse.success(message, taches));
    }

    // ============================================================
    //  GET /api/v1/tasks/{id} — Récupérer une tâche par ID
    // ============================================================

    /**
     * Récupère une tâche spécifique par son identifiant.
     *
     * @param id l'identifiant de la tâche
     * @return 200 OK avec la tâche, ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    @Operation(
        summary     = "Récupérer une tâche",
        description = "Récupère le détail d'une tâche spécifique par son identifiant."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description  = "Tâche trouvée"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description  = "Tâche non trouvée"
        )
    })
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTacheParId(
            @Parameter(description = "Identifiant de la tâche", example = "1", required = true)
            @PathVariable Long id) {

        log.info("GET /api/v1/tasks/{} — Récupération de la tâche", id);
        TaskResponseDTO tache = taskService.getTacheParId(id);

        return ResponseEntity.ok(ApiResponse.success("Tâche trouvée", tache));
    }

    // ============================================================
    //  PUT /api/v1/tasks/{id} — Mettre à jour une tâche
    // ============================================================

    /**
     * Met à jour une tâche existante.
     *
     * @param id  l'identifiant de la tâche à modifier
     * @param dto les nouvelles données
     * @return 200 OK avec la tâche mise à jour
     */
    @PutMapping("/{id}")
    @Operation(
        summary     = "Mettre à jour une tâche",
        description = "Modifie le titre, la description ou le statut d'une tâche existante."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description  = "Tâche mise à jour avec succès"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description  = "Données invalides"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description  = "Tâche non trouvée"
        )
    })
    public ResponseEntity<ApiResponse<TaskResponseDTO>> mettreAJourTache(
            @Parameter(description = "Identifiant de la tâche", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO dto) {

        log.info("PUT /api/v1/tasks/{} — Mise à jour de la tâche", id);
        TaskResponseDTO tache = taskService.mettreAJourTache(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Tâche mise à jour avec succès", tache));
    }

    // ============================================================
    //  DELETE /api/v1/tasks/{id} — Supprimer une tâche
    // ============================================================

    /**
     * Supprime définitivement une tâche.
     *
     * @param id l'identifiant de la tâche à supprimer
     * @return 200 OK avec confirmation, ou 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary     = "Supprimer une tâche",
        description = "Supprime définitivement une tâche par son identifiant."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description  = "Tâche supprimée avec succès"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description  = "Tâche non trouvée"
        )
    })
    public ResponseEntity<ApiResponse<Void>> supprimerTache(
            @Parameter(description = "Identifiant de la tâche", example = "1", required = true)
            @PathVariable Long id) {

        log.info("DELETE /api/v1/tasks/{} — Suppression de la tâche", id);
        taskService.supprimerTache(id);

        return ResponseEntity.ok(ApiResponse.success("Tâche supprimée avec succès"));
    }
}
