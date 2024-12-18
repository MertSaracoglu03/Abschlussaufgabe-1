package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskList;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "list" command used to display tasks from a specific list in the task manager.
 * <p>
 * The command expects a single argument, which is the name of the list.
 * Upon execution, it displays the tasks
 * from the specified list.
 * If the list is not found, an error message is displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class ListCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "list";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int LIST_NAME_INDEX = 0;
    private static final String LIST_NOT_FOUND_ERROR_FORMAT
            = TaskManagerCommand.createError("LIST with name '%s' was not found.%n");

    /**
     * Constructs a new ListCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and displayed.
     */

    public ListCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "list" command. It retrieves and displays tasks from the specified list.
     *
     * @param commandArguments The arguments provided by the user for the "list" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        Optional<TaskList> list = taskManager.getList(commandArguments[LIST_NAME_INDEX]);
        if (list.isEmpty()) {
            System.err.printf(LIST_NOT_FOUND_ERROR_FORMAT.formatted(commandArguments[LIST_NAME_INDEX]));
            return;
        }

        list.get().sort();
        StringBuilder builder = new StringBuilder();
        for (Task task : list.get().getTasks()) {
            builder.append(printTasks(task, 0));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);
    }
}
