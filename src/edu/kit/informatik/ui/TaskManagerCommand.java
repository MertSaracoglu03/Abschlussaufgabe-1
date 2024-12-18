package edu.kit.informatik.ui;

import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;

/**
 * Represents a command specific to the task manager application.
 * This abstract class provides common functionality and validation for commands
 * that operate on tasks and task lists.
 * It also provides utility methods for
 * formatting and printing tasks.
 *
 * @author Programmieren-Team
 * @version 1.0
 */

public abstract class TaskManagerCommand extends Command {
    protected static final String NAME_REGEX = "^(?!.*[;" + System.lineSeparator() + "]).*$";
    protected static final String LIST_REGEX = "^[A-Za-z]+$";
    protected static final String TAG_REGEX = "^[A-Za-z0-9]+$";
    protected static final String WHITE_SPACE = " ";

    protected static final String INVALID_DATE_ERROR = createError("Given date is invalid.");
    protected static final String INVALID_ARGUMENTS_ERROR = createError("Given arguments are invalid.");
    protected static final String NEXT_LINE_REGEX = "\n";
    private static final String DATE_PATTERN = "uuuu-MM-dd";
    protected static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern(DATE_PATTERN).withResolverStyle(ResolverStyle.STRICT);
    private static final String EXPECTED_INNER_ARGUMENTS_ERROR_FORMAT
            = createError("invalid number of arguments");
    private static final String ERROR_PREFIX = "ERROR: ";

    protected final TaskManager taskManager;
    protected final Set<Task> printedTasks = new HashSet<>();
    private final int minNumberOfArguments;
    private final int maxNumberOfArguments;


    /**
     * Initializes a new task manager command with the specified attributes.
     *
     * @param commandName          Name of the command.
     * @param taskManager          Reference to the task manager.
     * @param minNumberOfArguments Minimum number of arguments expected for this command.
     * @param maxNumberOfArguments Maximum number of arguments expected for this command.
     */


    protected TaskManagerCommand(String commandName, TaskManager taskManager,
                                 int minNumberOfArguments, int maxNumberOfArguments) {
        super(commandName);
        this.taskManager = taskManager;
        this.minNumberOfArguments = minNumberOfArguments;
        this.maxNumberOfArguments = maxNumberOfArguments;
    }

    /**
     * Executes the task manager command after validating the number of arguments.
     *
     * @param commandArguments Arguments provided for the command execution.
     */

    @Override
    public final void execute(String[] commandArguments) {
        List<String> arguments = new ArrayList<>();
        for (String commandArgument : commandArguments) {
            arguments.addAll(Arrays.asList(commandArgument.split(WHITE_SPACE)));
        }
        if (arguments.size() > maxNumberOfArguments || arguments.size() < minNumberOfArguments) {
            System.err.println(EXPECTED_INNER_ARGUMENTS_ERROR_FORMAT);
            return;
        }
        executeTaskManagerCommand(arguments.toArray(new String[0]));
    }

    /**
     * Abstract method to be implemented by subclasses to define the specific
     * behavior of the task manager command.
     *
     * @param commandArguments Arguments provided for the command execution.
     */
    protected abstract void executeTaskManagerCommand(String[] commandArguments);

    /**
     * Creates an error message with a standard prefix.
     *
     * @param message The specific error message.
     * @return Formatted error message.
     */

    protected static String createError(String message) {
        return ERROR_PREFIX + message;
    }

    /**
     * Checks if the provided name is valid based on a predefined regex pattern.
     *
     * @param name Name to be validated.
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */

    protected boolean isValidName(String name) {
        return !name.matches(NAME_REGEX);
    }


    /**
     * Prints the details of a task and its subtasks in a formatted manner.
     * Depending on the value of the onlyUndone parameter, this method can either print all tasks
     * or only those that are not marked as done.
     *
     * @param task  The task to be printed.
     * @param level The hierarchical level of the task, used for indentation.
     * @return the string presentation of the task.
     */

    protected String printTasks(Task task, int level) {
        if (task.isDeleted()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        appendTaskDetails(builder, task, level);
        for (Task subtask : task.getSubtasks()) {
            builder.append(printTasks(subtask, level + 2));
        }
        return builder.toString();
    }

    /**
     * Appends the details of the specified task to the provided StringBuilder.
     * The details include
     * the task's completion status, name, priority, tags, and date.
     * The task's details are indented
     * based on the specified level.
     *
     * @param builder The StringBuilder to which the task details are appended.
     * @param task    The task whose details are to be appended.
     * @param level   The indentation level for the task details.
     */

    protected void appendTaskDetails(StringBuilder builder, Task task, int level) {
        builder.append(WHITE_SPACE.repeat(level))
                .append("- ")
                .append(task.isDone() ? "[x] " : "[ ] ")
                .append(task.getName());

        if (task.getPriority() != null) {
            builder.append(" [").append(task.getPriority().name()).append("]");
        }

        if (!task.getTags().isEmpty() || task.getDate() != null) {
            builder.append(":");
        }

        if (!task.getTags().isEmpty()) {
            appendTags(builder, task);
        }

        if (task.getDate() != null) {
            builder.append(" --> ").append(task.getDate().toString());
        }

        printedTasks.add(task);
        builder.append(NEXT_LINE_REGEX);
    }

    /**
     * Appends the tags of the specified task to the provided StringBuilder.
     * The tags are enclosed
     * in parentheses and separated by commas.
     *
     * @param builder The StringBuilder to which the tags are appended.
     * @param task    The task whose tags are to be appended.
     */

    private void appendTags(StringBuilder builder, Task task) {
        builder.append(" (");
        List<String> tags = task.getTags();
        for (int i = 0; i < tags.size() - 1; i++) {
            builder.append(tags.get(i)).append(", ");
        }
        builder.append(tags.get(tags.size() - 1)).append(")");
    }
}
