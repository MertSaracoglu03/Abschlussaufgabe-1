
package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.model.TaskNotFoundException;
import edu.kit.informatik.ui.TaskManagerCommand;

/**
 * Represents the "restore" command which allows users to restore a previously deleted task.
 * When executed, this command restores a task and all its subtasks that were marked as deleted.
 * The task is identified by its unique ID.
 * <p>
 * The command expects a single argument: the ID of the task to be restored.
 * If the task with the given ID is found and was previously marked as deleted, it will be restored.
 * Otherwise, an error message will be displayed.
 *
 * @author utobm
 * @version 1.0
 */


public class RestoreCommand extends TaskManagerCommand {

    private static final String RESTORE_SUCCESS_FORMAT = "restored %s and %s subtasks%n";
    private static final String COMMAND_NAME = "restore";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int ID_INDEX = 0;

    /**
     * Initializes a new instance of the RestoreCommand class.
     *
     * @param taskManager The task manager instance to which this command is associated.
     */


    public RestoreCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the restore command.
     * This method attempts to restore a task and its subtasks
     * based on the provided command arguments.
     *
     * @param commandArguments The arguments provided for the command execution.
     */

    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        int id;
        try {
            id = Integer.parseInt(commandArguments[ID_INDEX]);
        } catch (NumberFormatException ignored) {
            System.err.println(TaskManagerCommand.INVALID_ARGUMENTS_ERROR);
            return;
        }
        Task task;
        try {
            task = taskManager.undoDelete(id);
        } catch (TaskNotFoundException e) {
            System.out.println(createError(e.getMessage()));
            return;
        }
        System.out.printf(RESTORE_SUCCESS_FORMAT.formatted(task.getName(), task.countAllSubtasks()));

    }
}


