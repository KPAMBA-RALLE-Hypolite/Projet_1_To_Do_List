package com.kfokam.todoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité JPA représentant une tâche dans la base de données.
 *
 * <p>Chaque tâche possède :</p>
 * <ul>
 *   <li>Un identifiant unique auto-généré</li>
 *   <li>Un titre obligatoire (max 100 caractères)</li>
 *   <li>Une description optionnelle (max 500 caractères)</li>
 *   <li>Un statut parmi : A_FAIRE, EN_COURS, TERMINE</li>
 *   <li>Des timestamps de création et mise à jour automatiques</li>
 * </ul>
 *
 * <p>Annotations Lombok utilisées :</p>
 * <ul>
 *   <li>{@code @Data}           - génère getters, setters, toString, equals, hashCode</li>
 *   <li>{@code @Builder}        - pattern Builder pour la construction d'objets</li>
 *   <li>{@code @NoArgsConstructor} - constructeur sans argument (requis par JPA)</li>
 *   <li>{@code @AllArgsConstructor} - constructeur avec tous les arguments</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
@Entity                          // Indique que cette classe est une entité JPA
@Table(name = "tasks")           // Nom de la table en base de données
@Data                            // Lombok : génère getters/setters/toString/equals/hashCode
@Builder                         // Lombok : pattern Builder
@NoArgsConstructor               // Lombok : constructeur vide (requis par JPA)
@AllArgsConstructor              // Lombok : constructeur complet
public class Task {

    /**
     * Identifiant unique de la tâche, généré automatiquement par la BDD.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titre de la tâche.
     * Obligatoire, entre 1 et 100 caractères.
     */
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    @Column(name = "titre", nullable = false, length = 100)
    private String titre;

    /**
     * Description détaillée de la tâche.
     * Optionnelle, max 500 caractères.
     */
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Statut actuel de la tâche.
     * Valeur par défaut : A_FAIRE
     * Stocké comme chaîne de caractères en base (EnumType.STRING).
     */
    @Enumerated(EnumType.STRING)  // Stocke "A_FAIRE", "EN_COURS" ou "TERMINE" (pas un entier)
    @Column(name = "statut", nullable = false)
    @Builder.Default               // Lombok Builder : valeur par défaut dans le builder
    private TaskStatus statut = TaskStatus.A_FAIRE;

    /**
     * Date et heure de création de la tâche.
     * Remplie automatiquement à l'insertion, non modifiable.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date et heure de la dernière mise à jour.
     * Mise à jour automatiquement à chaque modification.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
