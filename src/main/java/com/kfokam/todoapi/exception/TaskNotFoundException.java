package com.kfokam.todoapi.exception;

/**
 * Exception levée lorsqu'une tâche est introuvable en base de données.
 *
 * <p>Cette exception est une {@link RuntimeException} non vérifiée,
 * ce qui signifie qu'elle n'a pas besoin d'être déclarée dans la
 * signature des méthodes.</p>
 *
 * <p>Elle est interceptée par le {@link GlobalExceptionHandler}
 * qui retourne une réponse HTTP 404 au client.</p>
 *
 * @author KFOKAM48
 * @version 1.0
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant l'erreur
     */
    public TaskNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur pratique qui génère automatiquement le message
     * à partir de l'ID de la tâche non trouvée.
     *
     * @param id l'identifiant de la tâche introuvable
     */
    public TaskNotFoundException(Long id) {
        super("Tâche non trouvée avec l'ID : " + id);
    }
}
