package edu.kit.informatik.model;

/**
 * Exception thrown when a list with a specific name is not found.
 *
 * @author utobm
 * @version 1.0
 */
public class ListNotFoundException extends Exception {

    private static final String LIST_NOT_FOUND_ERROR_FORMAT = "List with name '%s' was not found.";

    /**
     * Constructs a new exception with a formatted error message.
     *
     * @param name The name of the list that was not found.
     */
    public ListNotFoundException(String name) {
        super(LIST_NOT_FOUND_ERROR_FORMAT.formatted(name));
    }

}



