package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "toggle" command used to change the completion status of a task.
 * The command expects a single argument: the ID of the task whose completion status is to be toggled.
 * Upon execution, if the task is currently marked as done, it will be marked as not done, and vice versa.
 * The command will also display a success message indicating the task and its subtasks that were toggled.
 *
 * @author utobm
 * @version 1.0
 */

public class ToggleCommand extends TaskManagerCommand {
    private static final String TOGGLE_SUCCESS_FORMAT = "toggled %s and %s subtasks%n";
    private static final String TASK_NOT_FOUND_ERROR_FORMAT = createError("Task with ID '%d' was not found.%n");

    private static final String COMMAND_NAME = "toggle";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int ID_INDEX = 0;

    /**
     * Constructs a new ToggleCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and toggled.
     */

    public ToggleCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "toggle" command. It retrieves the task with the specified ID and toggles its completion status.
     *
     * @param commandArguments The arguments provided by the user for the "toggle" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        int id;
        try {
            id = Integer.parseInt(commandArguments[ID_INDEX]);
        } catch (NumberFormatException ignored) {
            System.err.println(INVALID_ARGUMENTS_ERROR);
            return;
        }
        Optional<Task> task = taskManager.getTask(id);
        if (task.isEmpty() || task.get().isDeleted()) {
            System.err.printf(TASK_NOT_FOUND_ERROR_FORMAT.formatted(id));
            return;
        }
        task.get().toggle();
        System.out.printf(TOGGLE_SUCCESS_FORMAT.formatted(task.get().getName(), task.get().countAllSubtasks()));
    }
}
