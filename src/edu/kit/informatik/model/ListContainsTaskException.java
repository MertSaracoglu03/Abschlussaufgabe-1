package edu.kit.informatik.model;

/**
 * Exception thrown when the list already contains the task.
 *
 * @author utobm
 * @version 1.0
 */
public class ListContainsTaskException extends Exception {

    private static final String TASK_ALREADY_IN_LIST_ERROR_FORMAT = "List already contains the Task with ID '%d'.";

    /**
     * Constructs a new exception with a formatted error message.
     *
     * @param id The id of the task, which the list already contains.
     */
    public ListContainsTaskException(int id) {
        super(TASK_ALREADY_IN_LIST_ERROR_FORMAT.formatted(id));
    }

}



