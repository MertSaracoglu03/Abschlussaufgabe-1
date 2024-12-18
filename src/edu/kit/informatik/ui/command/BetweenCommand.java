package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents the "between" command used to display tasks that fall between two specified dates.
 * This command allows users to filter tasks based on a date range.
 * The command expects two date arguments
 * in the format "yyyy-MM-dd".
 * It then lists all tasks that have a date falling between the provided range.
 * If the second date is before the first date, the command swaps them to ensure a valid range.
 *
 * @author utobm
 * @version 1.0
 */

public class BetweenCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "between";
    private static final String INVALID_DATE_ERROR = TaskManagerCommand.createError("Date is invalid.");
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int FIRST_DATE_INDEX = 0;
    private static final int SECOND_DATE_INDEX = 1;

    /**
     * Constructs a new BetweenCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and filtered.
     */
    public BetweenCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "between" command.
     * Validates the provided date range and if valid,
     * lists all tasks that fall between the specified dates.
     *
     * @param commandArguments The arguments provided by the user for the "between" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        LocalDate firstDate;
        LocalDate secondDate;
        try {
            firstDate = LocalDate.parse(commandArguments[FIRST_DATE_INDEX], TaskManagerCommand.DATE_FORMATTER);
            secondDate = LocalDate.parse(commandArguments[SECOND_DATE_INDEX], TaskManagerCommand.DATE_FORMATTER);

            if (secondDate.isBefore(firstDate)) {
                LocalDate temp = firstDate;
                firstDate = secondDate;
                secondDate = temp;
            }
        } catch (DateTimeParseException ignored) {
            System.err.println(INVALID_DATE_ERROR);
            return;
        }
        taskManager.sort();
        printedTasks.clear();
        StringBuilder builder = new StringBuilder();
        for (Task task : taskManager.getTasks()) {
            builder.append(printBetweenTasks(task, firstDate, secondDate));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);
    }

    /**
     * Generates a string representation of the specified task and its subtasks that fall between
     * the given date range.
     * The method recursively checks and appends subtasks that also fall within
     * the specified range.
     * Tasks that have already been printed are skipped.
     *
     * @param task       The task to be checked and printed.
     * @param firstDate  The start date of the range.
     * @param secondDate The end date of the range.
     * @return A string representation of the task and its subtasks that fall within the date range.
     */


    private String printBetweenTasks(Task task, LocalDate firstDate, LocalDate secondDate) {
        StringBuilder builder = new StringBuilder();
        if (!printedTasks.contains(task)
                && isTaskBetween(task, firstDate, secondDate)) {
            builder.append(printTasks(task, 0));
        }

        for (Task subtask : task.getSubtasks()) {
            builder.append(printBetweenTasks(subtask, firstDate, secondDate));
        }
        return builder.toString();
    }

    /**
     * Checks if the specified task's date falls between the given date range, inclusive of the start date
     * and exclusive of the end date.
     *
     * @param task       The task whose date is to be checked.
     * @param firstDate  The start date of the range.
     * @param secondDate The end date of the range.
     * @return {@code true} if the task's date is between the specified range, otherwise {@code false}.
     */


    private boolean isTaskBetween(Task task, LocalDate firstDate, LocalDate secondDate) {
        return task.getDate() != null
                && (task.getDate().isEqual(firstDate) || task.getDate().isAfter(firstDate))
                && (task.getDate().isEqual(secondDate) || task.getDate().isBefore(secondDate));
    }
}
