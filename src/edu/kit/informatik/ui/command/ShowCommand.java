package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "show" command used to display a task and its subtasks in the task manager.
 * <p>
 * The command expects a single argument, which is the ID of the task to be displayed.
 * Upon execution,
 * it displays the task and all its subtasks in a hierarchical manner.
 * If the task is not found,
 * an error message is displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class ShowCommand extends TaskManagerCommand {
    private static final String TASK_NOT_FOUND_ERROR_FORMAT = TaskManagerCommand.createError("Task with ID '%d' was not found.%n");
    private static final String COMMAND_NAME = "show";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int ID_INDEX = 0;

    /**
     * Constructs a new ShowCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and displayed.
     */

    public ShowCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "show" command.
     * It retrieves the task with the specified ID and displays it
     * along with its subtasks in a hierarchical manner.
     *
     * @param commandArguments The arguments provided by the user for the "show" command.
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
        if (task.isEmpty()) {
            System.err.printf(TASK_NOT_FOUND_ERROR_FORMAT.formatted(id));
            return;
        }
        task.get().sort();
        printedTasks.clear();
        System.out.print(printTasks(task.get(), 0));
    }

}
