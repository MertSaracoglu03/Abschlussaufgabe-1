package edu.kit.informatik.model;

/**
 * Exception thrown when a task with a specific ID is not found.
 * @author utobm
 * @version 1.0
 */
public class TaskNotFoundException extends Exception {


    private static final String TASK_NOT_FOUND_ERROR_FORMAT = "Task with ID '%d' was not found.";

    /**
     * Constructs a new exception with a formatted error message.
     *
     * @param taskId The ID of the task that was not found.
     */

    public TaskNotFoundException(int taskId) {
        super(TASK_NOT_FOUND_ERROR_FORMAT.formatted(taskId));
    }
}
