package edu.kit.informatik.ui;

/**
 * Represents an abstract command that can be executed.
 * Provides a foundation for defining specific commands with unique names and execution behaviors.
 * Each subclass should implement the {@code execute} method to define its specific behavior.
 *
 * @author Programmieren-Team
 * @version 1.0
 */

public abstract class Command {
    /**
     * Name of the command.
     */
    private final String commandName;

    /**
     * Initializes a new command with the specified name.
     *
     * @param commandName Name of the command.
     */

    protected Command(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Retrieves the name of the command.
     *
     * @return The name of the command.
     */

    public String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command with the provided arguments.
     * This method should be implemented by subclasses to define the specific behavior of the command.
     *
     * @param commandArguments Arguments provided for the command execution.
     */

    public abstract void execute(String[] commandArguments);
}
