package edu.kit.informatik;

import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.CommandHandler;


/**
 * The {@code Main} class serves as the entry point for the application.
 * It initializes the {@link TaskManager} and {@link CommandHandler}
 * to handle user input and manage tasks.
 *
 * @author Programmieren-Team
 * @version 1.0
 */

public final class Main {


    private Main() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * The main method that serves as the starting point for the application.
     * It initializes the necessary components and starts the user input handling loop.
     *
     * @param args Command-line arguments passed to the application. Currently not used.
     */
    public static void main(String[] args) {
        // Initialize the task manager to manage tasks.
        TaskManager taskManager = new TaskManager();
        // Initialize the command handler to process user input.
        CommandHandler commandHandler = new CommandHandler(taskManager);
        // Start the user input handling loop.
        commandHandler.handleUserInput();

    }
}
