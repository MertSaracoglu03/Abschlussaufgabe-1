package edu.kit.informatik.model;

/**
 * Represents the priority levels for tasks.
 * Each priority level is associated with a unique integer value.
 *
 * @author utobm
 * @version 1.0
 */


public enum Priority {
    /**
     * High-priority level.
     */
    HI(1),
    /**
     * Medium priority level.
     */
    MD(2),
    /**
     * Low-priority level.
     */
    LO(3);

    /**
     * The integer value associated with the priority level.
     */
    private final int priority;

    /**
     * Constructs a new priority level with the specified integer value.
     *
     * @param priority The integer value associated with the priority level.
     */

    Priority(int priority) {
        this.priority = priority;
    }

    /**
     * Retrieves the priority level based on its name.
     *
     * @param expression The name of the priority level.
     * @return The corresponding priority level, or null if not found.
     */


    public static Priority getPriority(String expression) {
        for (Priority priority : Priority.values()) {
            if (expression.equals(priority.name())) {
                return priority;
            }
        }
        return null;
    }

    /**
     * Retrieves the integer value associated with the priority level.
     *
     * @return The integer value of the priority level.
     */

    public int getPriority() {
        return priority;
    }
}
