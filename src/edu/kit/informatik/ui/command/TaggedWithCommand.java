package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Priority;
import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.*;

/**
 * Represents the "tagged-with" command used to retrieve and display tasks that are tagged with a specific tag.
 * The command expects a single argument:
 * 1. The tag to be searched for.
 * Upon execution, it retrieves all tasks that are tagged with the specified tag and displays them.
 * If a task's ancestor (parent, grandparent, etc.) is tagged with the specified tag, the task itself is not displayed.
 *
 * @author utobm
 * @version 1.0
 */


public class TaggedWithCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "tagged-with";
    private static final String INVALID_TAG_ERROR = TaskManagerCommand.createError("The tag %s is invalid.%n");
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int TAG_INDEX = 0;

    /**
     * Constructs a new TaggedWithCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and checked for the specified tag.
     */


    public TaggedWithCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "tagged-with" command.
     * It retrieves all tasks that are tagged with the specified tag
     * and displays them.
     *
     * @param commandArguments The arguments provided by the user for the "tagged-with" command.
     */

    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        String tag = commandArguments[TAG_INDEX];
        if (!tag.matches(TAG_REGEX)) {
            System.err.printf(INVALID_TAG_ERROR.formatted(tag));
            return;
        }
        printedTasks.clear();
        Queue<Task> taskQueue = new LinkedList<>(taskManager.getTasks());
        List<Task> tasksToPrint = new ArrayList<>();

        while (!taskQueue.isEmpty()) {
            Task currentTask = taskQueue.poll();
            if (currentTask.getTags().contains(tag)) {
                tasksToPrint.add(currentTask);
            } else {
                taskQueue.addAll(currentTask.getSubtasks());
            }
        }
        sort(tasksToPrint);
        StringBuilder builder = new StringBuilder();
        for (Task task : tasksToPrint) {
            builder.append(printTasks(task, 0));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);

    }

    /**
     * Sorts the provided list of tasks based on their priority and ID. If the tasks do not have the same parent,
     * they are sorted first by priority (with null priorities placed last) and then by ID.
     * If the tasks have the
     * same parent, they are sorted only by priority.
     *
     * @param tasksToSort The list of tasks to be sorted.
     */

    private void sort(List<Task> tasksToSort) {
        if (tasksToSort.isEmpty()) {
            return;
        }
        boolean hasSameParent = hasSameParent(tasksToSort);
        if (!hasSameParent) {
            tasksToSort.sort(Comparator
                    .comparing(Task::getPriority,
                            Comparator.nullsLast(Comparator.comparingInt(Priority::getPriority))).thenComparing(Task::getId));
            return;
        }
        tasksToSort.sort(Comparator
                .comparing(Task::getPriority,
                        Comparator.nullsLast(Comparator.comparingInt(Priority::getPriority))));

    }

    /**
     * Checks if the provided list of tasks all have the same parent.
     * The method retrieves the parent of the first task
     * and then checks if all other tasks in the list are subtasks of this parent.
     *
     * @param tasks The list of tasks to be checked.
     * @return {@code true} if all tasks in the list have the same parent, otherwise {@code false}.
     */

    private boolean hasSameParent(List<Task> tasks) {
        if (tasks == null) {
            return false;
        }
        Optional<Task> parent = taskManager.getParent(tasks.get(0).getId());
        if (parent.isEmpty()) {
            return false;
        }
        for (Task task : tasks) {
            if (!parent.get().getSubtasks().contains(task)) {
                return false;
            }
        }
        return true;
    }
}
