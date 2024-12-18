package edu.kit.informatik.ui;

/**
 * Represents the "quit" command that terminates the command handler's execution.
 * This command does not accept any arguments and stops the command handler from processing further input.
 *
 * @author Programmieren-Team
 * @version 1.0
 */

public class QuitCommand extends Command {
    /**
     * Name of the quit command.
     */
    private static final String COMMAND_NAME = "quit";
    /**
     * Error message for when arguments are provided to the quit command.
     */
    private static final String QUIT_WITH_ARGUMENTS_ERROR = "ERROR: quit does not allow args.";
    /**
     * The command handler associated with this quit command.
     */
    private final CommandHandler commandHandler;

    /**
     * Initializes a new quit command with the specified command handler.
     *
     * @param commandHandler The command handler to be associated with this quit command.
     */

    public QuitCommand(CommandHandler commandHandler) {
        super(COMMAND_NAME);
        this.commandHandler = commandHandler;
    }

    /**
     * Executes the quit command.
     * If any arguments are provided, an error message is displayed.
     * Otherwise, the command handler's {@code quit} method is invoked.
     *
     * @param commandArguments Arguments provided for the command execution.
     */

    @Override
    public void execute(String[] commandArguments) {
        if (commandArguments.length != 0) {
            System.err.println(QUIT_WITH_ARGUMENTS_ERROR);
            return;
        }
        commandHandler.quit();
    }
}
