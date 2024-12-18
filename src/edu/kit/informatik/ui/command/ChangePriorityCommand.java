package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Priority;
import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "change-priority" command used to modify the priority of a specific task.
 * This command allows users to change the priority of a task identified by its unique ID.
 * The command expects two arguments: the ID of the task and the new priority.
 * If the provided ID does not correspond to any task or if the priority is invalid,
 * appropriate error messages are displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class ChangePriorityCommand extends TaskManagerCommand {
    private static final String TASK_NOT_FOUND_ERROR_FORMAT
            = TaskManagerCommand.createError("Task with ID '%d' was not found.%n");
    private static final String CHANGE_PRIORITY_SUCCESS_FORMAT = "changed %s to %s%n";
    private static final String PRIORITY_ERROR = TaskManagerCommand.createError("There is not such a priority type.");
    private static final String COMMAND_NAME = "change-priority";
    private static final int MIN_NUMBER_OF_ARGUMENTS = 1;
    private static final int MAX_NUMBER_OF_ARGUMENTS = 2;
    private static final int ID_INDEX = 0;
    private static final int PRIORITY_INDEX = 1;

    /**
     * Constructs a new ChangePriorityCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and modified.
     */
    public ChangePriorityCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, MIN_NUMBER_OF_ARGUMENTS, MAX_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "change-priority" command.
     * Validates the provided task ID and priority.
     * If both are valid, it changes the priority of the specified task.
     *
     * @param commandArguments The arguments provided by the user for the "change-priority" command.
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
        Optional<Task> task = taskManager.getTask(id);
        if (task.isEmpty() || task.get().isDeleted()) {
            System.err.printf(TASK_NOT_FOUND_ERROR_FORMAT.formatted(id));
            return;
        }
        if (commandArguments.length == MIN_NUMBER_OF_ARGUMENTS) {
            task.get().changePriority(null);
            System.out.printf(CHANGE_PRIORITY_SUCCESS_FORMAT.formatted(task.get().getName(), ""));
            return;
        }
        Priority priority = Priority.getPriority(commandArguments[PRIORITY_INDEX]);
        if (priority == null) {
            System.out.println(PRIORITY_ERROR);
            return;
        }
        task.get().changePriority(priority);
        System.out.printf(CHANGE_PRIORITY_SUCCESS_FORMAT.formatted(task.get().getName(), priority.name()));
    }
}
