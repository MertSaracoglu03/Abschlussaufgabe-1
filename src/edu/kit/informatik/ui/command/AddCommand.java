package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Priority;
import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents the "add" command used to add a new task to the task manager.
 * This command can add a task with just a name, or with additional attributes like priority and date.
 * The command expects between one or two or three arguments.
 * If the provided arguments are valid, a new task is added to the task manager, and a success message is displayed.
 * Otherwise, an appropriate error message is shown.
 *
 * @author utobm
 * @version 1.0
 */

public class AddCommand extends TaskManagerCommand {
    private static final String INVALID_NAME_ERROR = TaskManagerCommand.createError("Task name is invalid.");
    private static final String INVALID_PRIORITY_OR_DATE_ERROR = TaskManagerCommand.createError("Date or priority is invalid.");
    private static final String INVALID_DATE_ERROR = TaskManagerCommand.createError("Date is invalid.");
    private static final String INVALID_PRIORITY_ERROR = TaskManagerCommand.createError("Priority is invalid.");
    private static final String TASK_SUCCESS_FORMAT = "added %s: %s%n";
    private static final String COMMAND_NAME = "add";
    private static final int MIN_NUMBER_OF_ARGUMENTS = 1;
    private static final int MAX_NUMBER_OF_ARGUMENTS = 3;
    private static final int NAME_INDEX = 0;
    private static final int PRIORITY_INDEX = 1;
    private static final int DATE_INDEX = 2;

    /**
     * Constructs a new AddCommand with the specified task manager.
     *
     * @param taskManager The task manager to which tasks will be added.
     */

    public AddCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, MIN_NUMBER_OF_ARGUMENTS, MAX_NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "add" command.
     * Validates the provided arguments and adds a new task to the task manager.
     * Displays a success message if the task is added successfully, otherwise shows an error message.
     *
     * @param commandArguments The arguments provided by the user for the "add" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        String name = commandArguments[NAME_INDEX];
        if (isValidName(name)) {
            System.err.println(INVALID_NAME_ERROR);
            return;
        }

        Priority priority = null;
        LocalDate date = null;

        if (commandArguments.length == 2) {
            priority = Priority.getPriority(commandArguments[PRIORITY_INDEX]);
            date = parseDate(commandArguments[PRIORITY_INDEX]);
            if (priority == null && date == null) {
                System.err.println(INVALID_PRIORITY_OR_DATE_ERROR);
                return;
            }
        }

        if (commandArguments.length == MAX_NUMBER_OF_ARGUMENTS) {
            priority = Priority.getPriority(commandArguments[PRIORITY_INDEX]);
            date = parseDate(commandArguments[DATE_INDEX]);
            if (priority == null) {
                System.err.println(INVALID_PRIORITY_ERROR);
                return;
            }
            if (date == null) {
                System.err.println(INVALID_DATE_ERROR);
                return;
            }
        }


        Task task = taskManager.addTask(name, priority, date);
        System.out.printf(TASK_SUCCESS_FORMAT, task.getId(), task.getName());
    }

    /**
     * Parses the given date string into a LocalDate object.
     * If the date string is invalid, returns null.
     *
     * @param dateStr The date string to be parsed.
     * @return A LocalDate object if the date string is valid, otherwise null.
     */

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, TaskManagerCommand.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
