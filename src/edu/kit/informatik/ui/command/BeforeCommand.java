package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


/**
 * Represents the "before" command used to display tasks that are due before a specified date.
 * This command retrieves and displays tasks from the task manager that have a due date before (or on)
 * the provided date.
 * The command expects exactly one argument: the date in the format "yyyy-MM-dd".
 * If the provided date is valid, tasks due before that date are displayed.
 * Otherwise, an appropriate error
 * message is shown.
 *
 * @author utobm
 * @version 1.0
 */


public class BeforeCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "before";
    private static final String INVALID_DATE_ERROR = TaskManagerCommand.createError("Date is invalid.");
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int DATE_INDEX = 0;

    /**
     * Constructs a new BeforeCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved.
     */


    public BeforeCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "before" command.
     * Validates the provided date and displays tasks from the task manager
     * that are due before the specified date.
     *
     * @param commandArguments The arguments provided by the user for the "before" command.
     */

    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        LocalDate date;
        try {
            date = LocalDate.parse(commandArguments[DATE_INDEX], TaskManagerCommand.DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
            System.err.println(INVALID_DATE_ERROR);
            return;
        }
        taskManager.sort();
        printedTasks.clear();
        StringBuilder builder = new StringBuilder();
        for (Task task : taskManager.getTasks()) {
            builder.append(printTasksBefore(task, date));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);
    }


    /**
     * Prints tasks and their subtasks that are due before the specified date.
     *
     * @param task The task to start with.
     * @param date The date to compare against.
     */
    private String printTasksBefore(Task task, LocalDate date) {
        StringBuilder builder = new StringBuilder();
        if (task.getDate() != null && !printedTasks.contains(task)
                && (task.getDate().isBefore(date) || task.getDate().isEqual(date))) {
            builder.append(printTasks(task, 0));
        }

        for (Task subtask : task.getSubtasks()) {
            builder.append(printTasksBefore(subtask, date));
        }
        return builder.toString();
    }
}
