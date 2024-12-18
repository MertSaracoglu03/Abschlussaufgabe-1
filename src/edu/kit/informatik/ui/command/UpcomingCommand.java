package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


/**
 * Represents the "upcoming" command used to display tasks that are due within the next week.
 * The command expects a single argument: a date.
 * It will then display all tasks that are due
 * from that date up to one week after the provided date.
 * If a task has a due date that falls within the specified range and none of its ancestors
 * (parent tasks) have a due date within that range, then the task will be displayed.
 *
 * @author utobm
 * @version 1.0
 */


public class UpcomingCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "upcoming";
    private static final String INVALID_DATE_ERROR = createError("Date is invalid.");
    private static final int DAYS_TO_ADD = 7;
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int DATE_INDEX = 0;

    /**
     * Constructs a new UpcomingCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and displayed.
     */

    public UpcomingCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "upcoming" command.
     * It retrieves tasks with due dates within the next week
     * from the provided date and displays them.
     *
     * @param commandArguments The arguments provided by the user for the "upcoming" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        LocalDate date;
        try {
            date = LocalDate.parse(commandArguments[DATE_INDEX], DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
            System.err.println(INVALID_DATE_ERROR);
            return;
        }
        taskManager.sort();
        printedTasks.clear();
        StringBuilder builder = new StringBuilder();
        for (Task task : taskManager.getTasks()) {
            builder.append(printUpcomingTasks(task, date));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);
    }
    /**
     * Generates a string representation of the specified task and its subtasks that are upcoming
     * based on the given date.
     * The method recursively checks and appends subtasks that are also
     * upcoming.
     * Tasks that have already been printed are skipped.
     *
     * @param task The task to be checked and printed.
     * @param date The reference date to determine if a task is upcoming.
     * @return A string representation of the task and its subtasks that are upcoming based on the date.
     */


    private String printUpcomingTasks(Task task, LocalDate date) {
        StringBuilder builder = new StringBuilder();
        if (isTaskUpcoming(task, date) && !printedTasks.contains(task)) {
            builder.append(printTasks(task, 0));
        }

        for (Task subtask : task.getSubtasks()) {
            builder.append(printUpcomingTasks(subtask, date));
        }
        return builder.toString();


    }
    /**
     * Checks if the specified task's date is upcoming based on the given reference date.
     * A task is
     * considered upcoming if its date is on or after the reference date and before the reference date
     * plus a predefined number of days (DAYS_TO_ADD).
     *
     * @param task The task whose date is to be checked.
     * @param date The reference date.
     * @return {@code true} if the task's date is upcoming based on the reference date, otherwise {@code false}.
     */


    private boolean isTaskUpcoming(Task task, LocalDate date) {
        if (task.getDate() == null) {
            return false;
        }
        LocalDate upcomingDate = date.plusDays(DAYS_TO_ADD);
        if (task.getDate().isBefore(date)) {
            return false;
        }
        return !task.getDate().isAfter(upcomingDate);
    }

}
