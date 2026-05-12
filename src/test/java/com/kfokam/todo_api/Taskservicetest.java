package com.kfokam.todo_api;

import com.kfokam.todoapi.dto.TaskRequestDTO;
import com.kfokam.todoapi.dto.TaskResponseDTO;
import com.kfokam.todoapi.exception.TaskNotFoundException;
import com.kfokam.todoapi.model.Task;
import com.kfokam.todoapi.model.TaskStatus;
import com.kfokam.todoapi.repository.TaskRepository;
import com.kfokam.todoapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la couche service {@link TaskService}.
 *
 * <p>Utilise Mockito pour simuler le repository et tester la logique métier
 * de manière isolée, sans base de données réelle.</p>
 *
 * @author KFOKAM48
 */
@ExtendWith(MockitoExtension.class)  // Active Mockito avec JUnit 5
@DisplayName("Tests unitaires — TaskService")
class TaskServiceTest {

    @Mock                           // Crée un mock du repository
    private TaskRepository taskRepository;

    @InjectMocks                    // Injecte les mocks dans le service testé
    private TaskService taskService;

    private Task taskExemple;

    /**
     * Prépare les données de test avant chaque test.
     */
    @BeforeEach
    void setUp() {
        taskExemple = Task.builder()
                .id(1L)
                .titre("Tâche de test")
                .description("Description de test")
                .statut(TaskStatus.A_FAIRE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ===========================
    //  TESTS — CRÉER UNE TÂCHE
    // ===========================

    @Test
    @DisplayName("creerTache() → doit créer et retourner la tâche")
    void creerTache_doitRetournerTacheCree() {
        // GIVEN : préparer les données et simuler le repository
        TaskRequestDTO dto = TaskRequestDTO.builder()
                .titre("Nouvelle tâche")
                .description("Une description")
                .statut(TaskStatus.A_FAIRE)
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(taskExemple);

        // WHEN : appeler la méthode à tester
        TaskResponseDTO result = taskService.creerTache(dto);

        // THEN : vérifier le résultat
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitre()).isEqualTo("Tâche de test");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("creerTache() sans statut → doit utiliser A_FAIRE par défaut")
    void creerTache_sansStatut_doitUtiliserAFaireParDefaut() {
        // GIVEN
        TaskRequestDTO dto = TaskRequestDTO.builder()
                .titre("Tâche sans statut")
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(taskExemple);

        // WHEN
        taskService.creerTache(dto);

        // THEN : vérifier que save est appelé avec statut A_FAIRE
        verify(taskRepository).save(argThat(task ->
                task.getStatut() == TaskStatus.A_FAIRE
        ));
    }

    // ===========================
    //  TESTS — LIRE LES TÂCHES
    // ===========================

    @Test
    @DisplayName("getToutesTaches() sans filtre → doit retourner toutes les tâches")
    void getToutesTaches_sansFiltre_doitRetournerToutes() {
        // GIVEN
        when(taskRepository.findAll()).thenReturn(List.of(taskExemple));

        // WHEN
        List<TaskResponseDTO> result = taskService.getToutesTaches(null);

        // THEN
        assertThat(result).hasSize(1);
        verify(taskRepository).findAll();
        verify(taskRepository, never()).findByStatut(any());
    }

    @Test
    @DisplayName("getToutesTaches() avec filtre → doit filtrer par statut")
    void getToutesTaches_avecFiltre_doitFiltrerParStatut() {
        // GIVEN
        when(taskRepository.findByStatut(TaskStatus.A_FAIRE)).thenReturn(List.of(taskExemple));

        // WHEN
        List<TaskResponseDTO> result = taskService.getToutesTaches(TaskStatus.A_FAIRE);

        // THEN
        assertThat(result).hasSize(1);
        verify(taskRepository).findByStatut(TaskStatus.A_FAIRE);
        verify(taskRepository, never()).findAll();
    }

    @Test
    @DisplayName("getTacheParId() avec ID valide → doit retourner la tâche")
    void getTacheParId_idValide_doitRetournerTache() {
        // GIVEN
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskExemple));

        // WHEN
        TaskResponseDTO result = taskService.getTacheParId(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getTacheParId() avec ID inexistant → doit lever TaskNotFoundException")
    void getTacheParId_idInexistant_doitLeverException() {
        // GIVEN
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> taskService.getTacheParId(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ===========================
    //  TESTS — MISE À JOUR
    // ===========================

    @Test
    @DisplayName("mettreAJourTache() → doit mettre à jour et retourner la tâche")
    void mettreAJourTache_doitMettreAJourTache() {
        // GIVEN
        TaskRequestDTO dto = TaskRequestDTO.builder()
                .titre("Titre modifié")
                .statut(TaskStatus.EN_COURS)
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskExemple));
        when(taskRepository.save(any(Task.class))).thenReturn(taskExemple);

        // WHEN
        TaskResponseDTO result = taskService.mettreAJourTache(1L, dto);

        // THEN
        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("mettreAJourTache() avec ID inexistant → doit lever TaskNotFoundException")
    void mettreAJourTache_idInexistant_doitLeverException() {
        // GIVEN
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> taskService.mettreAJourTache(99L, new TaskRequestDTO()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    // ===========================
    //  TESTS — SUPPRESSION
    // ===========================

    @Test
    @DisplayName("supprimerTache() avec ID valide → doit supprimer la tâche")
    void supprimerTache_idValide_doitSupprimerTache() {
        // GIVEN
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // WHEN
        taskService.supprimerTache(1L);

        // THEN
        verify(taskRepository).deleteById(1L);
    }

    @Test
    @DisplayName("supprimerTache() avec ID inexistant → doit lever TaskNotFoundException")
    void supprimerTache_idInexistant_doitLeverException() {
        // GIVEN
        when(taskRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThatThrownBy(() -> taskService.supprimerTache(99L))
                .isInstanceOf(TaskNotFoundException.class);
        verify(taskRepository, never()).deleteById(any());
    }
}