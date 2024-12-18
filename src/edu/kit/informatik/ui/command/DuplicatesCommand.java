package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.*;

/**
 * Represents the "duplicates" command used to identify duplicate tasks in the task manager.
 * A task is considered a duplicate of another if they have the same name and either both have the same date
 * or only one of them has a date.
 * <p>
 * The command does not expect any arguments and upon execution, it displays the number of duplicate tasks
 * and their IDs in ascending order.
 *
 * @author utobm
 * @version 1.0
 */
public class DuplicatesCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "duplicates";
    private static final int NUMBER_OF_ARGUMENTS = 0;

    /**
     * Constructs a new DuplicatesCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and checked for duplicates.
     */

    public DuplicatesCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "duplicates" command. It identifies and lists the IDs of all duplicate tasks.
     *
     * @param commandArguments The arguments provided by the user for the "duplicates" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        List<Task> allTasks = new ArrayList<>();
        for (Task task : taskManager.getTasks()) {
            if (!task.isDeleted()) {
                allTasks.add(task);
                allTasks.addAll(getAllSubtasks(task));
            }
        }

        Set<Integer> duplicates = new HashSet<>();

        for (int i = 0; i < allTasks.size(); i++) {
            Task currentTask = allTasks.get(i);
            for (int j = i + 1; j < allTasks.size(); j++) {
                Task otherTask = allTasks.get(j);
                if (areTasksDuplicates(currentTask, otherTask)) {
                    duplicates.add(currentTask.getId());
                    duplicates.add(otherTask.getId());
                }
            }
        }

        List<Integer> sortedDuplicates = new ArrayList<>(duplicates);
        Collections.sort(sortedDuplicates);
        StringBuilder builder = new StringBuilder();
        builder.append("Found ").append(sortedDuplicates.size()).append(" duplicates: ");

        for (int i = 0; i < sortedDuplicates.size(); i++) {
            builder.append(sortedDuplicates.get(i));
            if (i < sortedDuplicates.size() - 1) {
                builder.append(", ");
            }
        }


        System.out.println(builder);
    }

    /**
     * Recursively retrieves all subtasks of a given task.
     *
     * @param task The task for which all subtasks are to be retrieved.
     * @return A list containing the task's subtasks and their subtasks.
     */

    private List<Task> getAllSubtasks(Task task) {
        List<Task> allSubtasks = new ArrayList<>(task.getSubtasks());
        for (Task subtask : task.getSubtasks()) {
            allSubtasks.addAll(getAllSubtasks(subtask));
        }
        return allSubtasks;
    }

    /**
     * Checks if two tasks are duplicates based on their names and dates.
     *
     * @param task1 The first task to be compared.
     * @param task2 The second task to be compared.
     * @return true if the tasks are duplicates, false otherwise.
     */

    private boolean areTasksDuplicates(Task task1, Task task2) {
        boolean haveSameName = task1.getName().equals(task2.getName());
        boolean dateCheck
                = (task1.getDate() == null && task2.getDate() == null)
                || (task1.getDate() != null && task1.getDate().equals(task2.getDate()))
                || (task1.getDate() == null ^ task2.getDate() == null);
        boolean areTasksDifferent = task1.getId() != task2.getId();

        return haveSameName && dateCheck && areTasksDifferent;
    }
}
