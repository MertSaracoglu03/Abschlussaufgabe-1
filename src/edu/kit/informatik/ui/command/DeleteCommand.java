package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "delete" command used to remove a specific task and its subtasks.
 * This command allows users to delete a task identified by its unique ID.
 * The command expects a single argument: the ID of the task to be deleted.
 * If the provided ID does not correspond to any task, an error message is displayed.
 * Upon successful deletion, a message indicating the name of the deleted task and
 * the number of its subtasks that were also deleted is displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class DeleteCommand extends TaskManagerCommand {
    private static final String TASK_NOT_FOUND_ERROR_FORMAT
            = TaskManagerCommand.createError("Task with ID '%d' was not found.%n");
    private static final String DELETE_SUCCESS_FORMAT = "deleted %s and %s subtasks%n";
    private static final String COMMAND_NAME = "delete";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int ID_INDEX = 0;

    /**
     * Constructs a new DeleteCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and deleted.
     */

    public DeleteCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "delete" command.
     * Validates the provided task ID.
     * If valid, it deletes the task and all its subtasks.
     *
     * @param commandArguments The arguments provided by the user for the "delete" command.
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
        int numberOfSubtask = task.get().countAllSubtasks();
        taskManager.delete(id);
        System.out.printf(DELETE_SUCCESS_FORMAT.formatted(task.get().getName(), numberOfSubtask));

    }
}
