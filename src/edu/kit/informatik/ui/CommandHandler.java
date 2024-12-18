package edu.kit.informatik.ui;


import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.command.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles user input and executes the corresponding commands.
 * Provides a mechanism to register, identify, and execute commands based on user input.
 * Supports a variety of commands related to task management.
 *
 * @author Programmieren-Team
 * @version 1.0
 */

public class CommandHandler {
    /**
     * Separator for splitting command input.
     */
    private static final String COMMAND_SEPARATOR = "\\s+";

    /**
     * Error message format for unrecognized commands.
     */
    private static final String COMMAND_NOT_FOUND = "ERROR: Command '%s' not found%n";
    /**
     * The task manager associated with this command handler.
     */
    private final TaskManager taskManager;
    /**
     * Map of registered commands.
     */
    private final Map<String, Command> commands;
    /**
     * Flag indicating whether the command handler is currently running.
     */
    private boolean running = false;

    /**
     * Initializes a new command handler with the specified task manager.
     *
     * @param taskManager The task manager to be used.
     */

    public CommandHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.commands = new HashMap<>();
        this.initCommands();

    }

    /**
     * Handles user input and executes the corresponding commands.
     */
    public void handleUserInput() {
        this.running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Stops the command handler from processing further input.
     */

    protected void quit() {
        this.running = false;
    }

    /**
     * Executes the specified command with its arguments.
     *
     * @param commandWithArguments The full command string with its arguments.
     */

    private void executeCommand(String commandWithArguments) {
        String[] splitCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR);
        String commandName = splitCommand[0];
        String[] arguments = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
        if (!commands.containsKey(commandName)) {
            System.err.printf(COMMAND_NOT_FOUND, commandName);
            return;
        }
        commands.get(commandName).execute(arguments);
    }

    /**
     * Registers a command with the command handler.
     *
     * @param command The command to be registered.
     */

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    /**
     * Initializes and registers all supported commands.
     */

    private void initCommands() {
        this.addCommand(new AddCommand(taskManager));
        this.addCommand(new AddListCommand(taskManager));
        this.addCommand(new TagCommand(taskManager));
        this.addCommand(new AssignCommand(taskManager));
        this.addCommand(new ToggleCommand(taskManager));
        this.addCommand(new ChangeDateCommand(taskManager));
        this.addCommand(new ChangePriorityCommand(taskManager));
        this.addCommand(new DeleteCommand(taskManager));
        this.addCommand(new RestoreCommand(taskManager));
        this.addCommand(new ShowCommand(taskManager));
        this.addCommand((new TodoCommand(taskManager)));
        this.addCommand(new ListCommand(taskManager));
        this.addCommand(new TaggedWithCommand(taskManager));
        this.addCommand(new FindCommand(taskManager));
        this.addCommand(new UpcomingCommand(taskManager));
        this.addCommand(new BeforeCommand(taskManager));
        this.addCommand(new BetweenCommand(taskManager));
        this.addCommand(new DuplicatesCommand(taskManager));
        this.addCommand(new QuitCommand(this));
    }
}
