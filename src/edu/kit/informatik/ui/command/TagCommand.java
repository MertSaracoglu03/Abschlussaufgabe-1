package edu.kit.informatik.ui.command;


import edu.kit.informatik.model.Task;
import edu.kit.informatik.model.TaskList;
import edu.kit.informatik.model.TaskManager;
import edu.kit.informatik.ui.TaskManagerCommand;

import java.util.Optional;

/**
 * Represents the "tag" command used to assign a tag to a task or a list in the task manager.
 * The command expects two arguments:
 * 1. The ID of the task or the name of the list to be tagged.
 * 2. The tag to be assigned.
 * Upon execution, it checks if the provided ID or name corresponds to a task or a list.
 * If a match is found,
 * the tag is assigned to the task or list.
 * If the tag has been used before, an error message is displayed.
 * If no task or list matches the provided ID or name, an error message is displayed.
 *
 * @author utobm
 * @version 1.0
 */

public class TagCommand extends TaskManagerCommand {
    private static final String TAG_SUCCESS_FORMAT = "tagged %s with %s%n";
    private static final String USED_TAG_ERROR = TaskManagerCommand.createError("The tag %s is used before.%n");
    private static final String INVALID_TAG_ERROR = TaskManagerCommand.createError("The tag %s is invalid.%n");
    private static final String TASK_NOT_FOUND_ERROR_FORMAT = TaskManagerCommand.createError("Task with ID '%d' was not found.%n");
    private static final String LIST_NOT_FOUND_ERROR_FORMAT = TaskManagerCommand.createError("List with name '%s' was not found.%n");
    private static final String COMMAND_NAME = "tag";
    private static final int NUMBER_OF_ARGUMENTS = 2;
    private static final int NAME_OR_ID_INDEX = 0;
    private static final int TAG_INDEX = 1;

    /**
     * Constructs a new TagCommand with the specified task manager.
     *
     * @param taskManager The task manager from which tasks and lists will be retrieved and tagged.
     */

    public TagCommand(TaskManager taskManager) {
        super(COMMAND_NAME, taskManager, NUMBER_OF_ARGUMENTS, NUMBER_OF_ARGUMENTS);
    }

    /**
     * Executes the "tag" command.
     * It retrieves the task or list with the specified ID or name and
     * assigns the provided tag to it.
     *
     * @param commandArguments The arguments provided by the user for the "tag" command.
     */
    @Override
    protected void executeTaskManagerCommand(String[] commandArguments) {
        String input = commandArguments[NAME_OR_ID_INDEX];
        String tag = commandArguments[TAG_INDEX];
        if (!tag.matches(TAG_REGEX)) {
            System.err.printf(INVALID_TAG_ERROR, tag);
            return;
        }

        try {
            int id = Integer.parseInt(input);
            Optional<Task> taskOptional = taskManager.getTask(id);
            if (taskOptional.isPresent() && !taskOptional.get().isDeleted()) {
                Task task = taskOptional.get();
                if (task.setTag(tag)) {
                    System.out.printf(TAG_SUCCESS_FORMAT, task.getName(), tag);
                } else {
                    System.err.printf(USED_TAG_ERROR, tag);
                }
            } else {

                System.err.printf(TASK_NOT_FOUND_ERROR_FORMAT, id);
            }
        } catch (NumberFormatException ignored) {
            Optional<TaskList> listOptional = taskManager.getList(input);
            if (listOptional.isPresent()) {
                TaskList list = listOptional.get();
                if (list.setTag(tag)) {
                    System.out.printf(TAG_SUCCESS_FORMAT, input, tag);
                } else {
                    System.err.printf(USED_TAG_ERROR, tag);
                }
            } else {
                System.err.printf(LIST_NOT_FOUND_ERROR_FORMAT, input);
            }
        }
    }
}
