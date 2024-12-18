package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

/**
 * Represents the "find"
 * command used to identify tasks in the task manager that contain a specific sequence in their name.
 * <p>
 * The command expects a single argument, which is the sequence to search for.
 * Upon execution, it displays the tasks
 * that contain the specified sequence in their name.
 * If a parent task contains the sequence, its subtasks are not
 * considered as matches even if they also contain the sequence.
 *
 * @author utobm
 * @version 1.0
 */

public class FindCommand extends TaskManagerCommand {
    private static final String COMMAND_NAME = "find";
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int SEQUENCE_INDEX = 0;

    /**
     * Constructs a new FindCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks will be retrieved and checked for the sequence.
     */

    public FindCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "find" command. It identifies and lists the tasks that contain the specified sequence in their name.
     *
     * @param commandArguments The arguments provided by the user for the "find" command.
     */

    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        String sequence = commandArguments[SEQUENCE_INDEX];
        taskManager.sort();
        printedTasks.clear();
        StringBuilder builder = new StringBuilder();
        for (Task task : taskManager.getTasks()) {
            builder.append(printTasksWithSameSequence(task, sequence));
        }
        if (builder.isEmpty()) {
            System.out.print(NEXT_LINE_REGEX);
            return;
        }
        System.out.print(builder);
    }


    /**
     * Prints the tasks that contain the specified sequence in their name, excluding tasks whose ancestors already
     * contain the sequence.
     *
     * @param task     The task to be checked.
     * @param sequence The sequence to search for in the task's name.
     */
    private String printTasksWithSameSequence(Task task, String sequence) {
        StringBuilder builder = new StringBuilder();

        if (task.getName().contains(sequence) && !printedTasks.contains(task)) {
            builder.append(printTasks(task, 0));
            printedTasks.add(task); // Mark the task as processed.
        }

        for (Task subtask : task.getSubtasks()) {
            builder.append(printTasksWithSameSequence(subtask, sequence));
        }

        return builder.toString();
    }

}
