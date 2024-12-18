package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.*;
import edu.kit.informatik.ui.TaskManagerCommand;

/**
 * Represents the "assign" command used to assign a task to another task or a list in the task manager.
 * This command assigns a task with a given ID to either another task with a given ID or a list with a given name.
 * The command expects exactly two arguments: the ID of the task to be assigned and either the ID of the task
 * to which it should be assigned or the name of the list to which it should be added.
 * If the provided IDs and/or name are valid and the assignment is possible, the task is assigned, and a success
 * message is displayed.
 * Otherwise, an appropriate error message is shown.
 *
 * @author utobm
 * @version 1.0
 */

public class AssignCommand extends TaskManagerCommand {
    private static final String SUCCESS_FORMAT = "assigned %s to %s%n";

    private static final String COMMAND_NAME = "assign";
    private static final int MIN_NUMBER_OF_ARGUMENTS = 2;
    private static final int MAX_NUMBER_OF_ARGUMENTS = 2;
    private static final int FIRST_ID_INDEX = 0;
    private static final int SECOND_ID_OR_NAME_INDEX = 1;

    /**
     * Constructs a new AssignCommand with the specified task manager.
     *
     * @param taskManager The task manager where tasks and lists will be assigned.
     */

    public AssignCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, MIN_NUMBER_OF_ARGUMENTS, MAX_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "assign" command.
     * Validates the provided IDs and/or name and assigns the task to another task
     * or a list in the task manager.
     * Displays a success message if the task is assigned successfully, otherwise
     * shows an error message.
     *
     * @param commandArguments The arguments provided by the user for the "assign" command.
     */


    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        int firstID;
        try {
            firstID = Integer.parseInt(commandArguments[FIRST_ID_INDEX]);
        } catch (NumberFormatException ignored) {
            System.err.println(TaskManagerCommand.INVALID_ARGUMENTS_ERROR);
            return;
        }
        String second = commandArguments[SECOND_ID_OR_NAME_INDEX];
        String name;
        try {

            name = taskManager.assignTask(firstID, second);
        } catch (TaskNotFoundException | ListNotFoundException | SameTaskException | ListContainsTaskException e) {
            System.err.println(TaskManagerCommand.createError(e.getMessage()));
            return;
        }
        if (name == null) {
            return;
        }
        // The existence of getTask(firstID).get() is checked in the assign method in TaskManager class.
        System.out.printf(SUCCESS_FORMAT.formatted(taskManager.getTask(firstID).get().getName(), name));
    }


}
