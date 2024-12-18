package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Represents the "change-date" command used to modify the date of a specific task.
 * This command allows users to change the date of a task identified by its unique ID.
 * The command expects two arguments: the ID of the task and the new date in the format "yyyy-MM-dd".
 * If the provided ID does not correspond to any task or if the date format is invalid,
 * appropriate error messages are displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class ChangeDateCommand extends TaskManagerCommand {
    private static final String TASK_NOT_FOUND_ERROR_FORMAT = TaskManagerCommand.createError("Task with ID '%d' was not found.%n");
    private static final String CHANGE_DATE_SUCCESS_FORMAT = "changed %s to %s%n";
    private static final String COMMAND_NAME = "change-date";
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int ID_INDEX = 0;
    private static final int DATE_INDEX = 1;

    /**
     * Constructs a new ChangeDateCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and modified.
     */
    public ChangeDateCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "change-date" command.
     * Validates the provided task ID and date format.
     * If both are valid, it changes the date of the specified task.
     *
     * @param commandArguments The arguments provided by the user for the "change-date" command.
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
        LocalDate date;
        try {
            date = LocalDate.parse(commandArguments[DATE_INDEX], TaskManagerCommand.DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
            System.err.println(INVALID_DATE_ERROR);
            return;
        }
        task.get().changeDate(date);
        System.out.printf(CHANGE_DATE_SUCCESS_FORMAT.formatted(task.get().getName(), date));
    }
}
