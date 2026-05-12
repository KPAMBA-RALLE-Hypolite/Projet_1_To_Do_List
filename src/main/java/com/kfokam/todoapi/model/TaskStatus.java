package com.kfokam.todoapi.model;

/**
 * Enumération représentant les différents statuts d'une tâche.
 *
 * <p>Une tâche peut avoir trois états distincts dans son cycle de vie :</p>
 * <ul>
 *   <li>{@link #A_FAIRE}   - La tâche est créée mais pas encore commencée</li>
 *   <li>{@link #EN_COURS}  - La tâche est en cours de traitement</li>
 *   <li>{@link #TERMINE}   - La tâche est complètement terminée</li>
 * </ul>
 *
 * @author KFOKAM48
 * @version 1.0
 */
public enum TaskStatus {

    /**
     * La tâche est à faire (état initial par défaut).
     */
    A_FAIRE,

    /**
     * La tâche est en cours d'exécution.
     */
    EN_COURS,

    /**
     * La tâche est terminée.
     */
    TERMINE
}
