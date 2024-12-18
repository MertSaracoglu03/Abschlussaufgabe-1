package edu.kit.informatik.model;

/**
 * Exception thrown when a task is attempted to be assigned as its own subtask.
 *
 * @author utobm
 * @version 1.0
 */
public class SameTaskException extends Exception {

    private static final String SAME_TASK_EXCEPTION = "Task can't be assigned as its own subtask.";

    /**
     * Constructs a new exception with a predefined error message.
     */
    public SameTaskException() {
        super(SAME_TASK_EXCEPTION.formatted());
    }
}


