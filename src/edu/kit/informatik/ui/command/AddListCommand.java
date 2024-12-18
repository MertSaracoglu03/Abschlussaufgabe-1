package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

/**
 * Represents the "add-list" command used to add a new task list to the task manager.
 * This command adds a list with a specified name.
 * The command expects exactly one argument, which is the name of the list.
 * If the provided name is valid and there isn't yet a list with the same name,
 * a new list is added to the task manager, and a success message is displayed.
 * Otherwise, an appropriate error message is shown.
 *
 * @author utobm
 * @version 1.0
 */

public class AddListCommand extends TaskManagerCommand {
    private static final String INVALID_NAME_ERROR = TaskManagerCommand.createError("List name is invalid.");
    private static final String NAME_ERROR = TaskManagerCommand.createError("There is already a list by this name.");
    private static final String LIST_SUCCESS_FORMAT = "added %s%n";
    private static final String COMMAND_NAME = "add-list";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int NAME_INDEX = 0;

    /**
     * Constructs a new AddListCommand with the specified task manager.
     *
     * @param taskManager The task manager to which task lists will be added.
     */

    public AddListCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "add-list" command.
     * Validates the provided name and adds a new task list to the task manager.
     * Displays a success message if the list is added successfully, otherwise shows an error message.
     *
     * @param commandArguments The arguments provided by the user for the "add-list" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        String name = commandArguments[NAME_INDEX];
        if (!name.matches(LIST_REGEX)) {
            System.err.println(INVALID_NAME_ERROR);
            return;
        }
        boolean check = taskManager.addList(name);
        if (check) {
            System.out.printf(LIST_SUCCESS_FORMAT.formatted(name));
            return;
        }
        System.err.println(NAME_ERROR);
    }
}
