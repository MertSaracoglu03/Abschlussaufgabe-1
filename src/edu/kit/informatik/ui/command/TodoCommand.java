package edu.kit.informatik.ui.command;

import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the "todo" command used to retrieve and display tasks that are not yet completed.
 * The command does not expect any arguments.
 * Upon execution, it retrieves all tasks that are not marked as done
 * and displays them in a hierarchical manner.
 * Each task is displayed with its name, completion status, priority,
 * tags, and due date.
 *
 * @author utobm
 * @version 1.0
 */
public class TodoCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "todo";
    private static final int NUMBER_OF_ARGUMENTS = 0;
    private final Set<Task> printedTasks = new HashSet<>();

    /**
     * Constructs a new {@code TodoCommand} with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and checked for their completion status.
     */
    public TodoCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "todo" command.
     * It retrieves all tasks that are not marked as done
     * and displays them in a hierarchical manner.
     *
     * @param commandArguments The arguments provided by the user for the "todo" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        taskManager.sort();
        printedTasks.clear();
        StringBuilder builder = new StringBuilder();
        for (Task task : taskManager.getTasks()) {
            builder.append(printTasks(task, 0));
        }
        System.out.print(builder.isEmpty() ? NEXT_LINE_REGEX : builder);
    }

    /**
     * Recursively prints tasks and their subtasks that are not marked as done.
     *
     * @param task  The task to be printed.
     * @param level The hierarchical level of the task, used for indentation.
     * @return The formatted string representation of the task and its subtasks.
     */
    @Override
    protected String printTasks(Task task, int level) {
        if (printedTasks.contains(task) || task.isDeleted() || task.isDone() && areAllSubtasksDone(task)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        appendTaskDetails(builder, task, level);  // Use the method from the base class

        for (Task subtask : task.getSubtasks()) {
            builder.append(printTasks(subtask, level + 2));
        }

        return builder.toString();
    }


    /**
     * Checks if all subtasks of a given task are marked as done.
     *
     * @param task The task whose subtasks are to be checked.
     * @return true if all subtasks are done, false otherwise.
     */
    private boolean areAllSubtasksDone(Task task) {
        return task.getSubtasks().stream().allMatch(subtask -> subtask.isDone() && areAllSubtasksDone(subtask));
    }
}
