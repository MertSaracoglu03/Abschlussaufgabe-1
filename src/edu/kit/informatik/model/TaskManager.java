package edu.kit.informatik.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Manages tasks and task lists.
 * Provides methods for adding, deleting, restoring, and sorting tasks and task lists.
 *
 * @author utbom
 * @version 1.0
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final List<TaskList> lists = new ArrayList<>();
    private int taskID = 1;

    /**
     * Adds a new task.
     *
     * @param name     The name of the task.
     * @param priority The priority of the task.
     * @param date     The due date of the task.
     * @return The newly created task.
     */
    public Task addTask(String name, Priority priority, LocalDate date) {
        Task task = new Task(name, priority, date, taskID++);
        tasks.add(task);
        return task;
    }

    /**
     * Gets all tasks.
     *
     * @return A list of all tasks.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Gets a task by its ID.
     *
     * @param id The ID of the task.
     * @return An Optional containing the task if found, otherwise empty.
     */
    public Optional<Task> getTask(int id) {
        return tasks.stream()
                .flatMap(task -> task.getTask(id).stream())
                .findFirst();
    }

    /**
     * Retrieves a task list by its name.
     *
     * @param name The name of the task list.
     * @return An Optional containing the task list if found, otherwise empty.
     */
    public Optional<TaskList> getList(String name) {
        return lists.stream()
                .filter(taskList -> taskList.getName().equals(name))
                .findFirst();
    }

    /**
     * Adds a new task list to the collection of task lists.
     *
     * <p>This method checks if a task list with the given name already exists.
     * If it does, the method returns {@code false} and does not add a new list.
     * Otherwise, it adds a new task list with the specified name and returns {@code true}.</p>
     *
     * @param name The name of the task list to be added.
     * @return {@code true} if the task list was added successfully.
     */

    public boolean addList(String name) {
        if (lists.stream().anyMatch(taskList -> taskList.getName().equals(name))) {
            return false;
        }
        lists.add(new TaskList(name));
        return true;
    }

    /**
     * Retrieves the parent of a task by its ID.
     *
     * @param id The ID of the task.
     * @return An Optional containing the parent task if found, otherwise empty.
     */

    public Optional<Task> getParent(int id) {
        return tasks.stream()
                .flatMap(task -> task.getParent(id).stream())
                .findFirst();
    }

    /**
     * Assigns a task to another task or a task list.
     * If the second argument is an ID, the first task is assigned as a subtask to the second task.
     * If the second argument is a name, the first task is added to the task list with that name.
     *
     * @param firstID The ID of the task to be assigned.
     * @param second  The ID or name of the target task or task list.
     * @return The name of the target task or task list.
     * @throws TaskNotFoundException     If a task with the given ID is not found.
     * @throws SameTaskException         If the firstID and secondID are the same.
     * @throws ListNotFoundException     If a task list with the given name is not found.
     * @throws ListContainsTaskException If the task list already contains the task.
     */

    public String assignTask(int firstID, String second) throws TaskNotFoundException,
            ListNotFoundException, SameTaskException, ListContainsTaskException {

        Task firstTask = validateFirstTask(firstID);

        Optional<TaskList> list = getList(second);
        if (list.isEmpty()) {
            return assignToTask(firstID, second, firstTask);
        } else {
            return assignToTaskList(firstID, firstTask, list.get());
        }
    }

    /**
     * Validates the task with the given ID. If the task is not found or is marked as deleted,
     * a TaskNotFoundException is thrown.
     *
     * @param firstID The ID of the task to be validated.
     * @return The validated task.
     * @throws TaskNotFoundException If the task with the given ID is not found or is deleted.
     */

    private Task validateFirstTask(int firstID) throws TaskNotFoundException {
        Optional<Task> firstTask = getTask(firstID);
        if (firstTask.isEmpty() || firstTask.get().isDeleted()) {
            throw new TaskNotFoundException(firstID);
        }
        return firstTask.get();
    }

    /**
     * Assigns the first task to the second task.
     * The second task becomes the parent of the first task.
     * If the second task is not found, is deleted, or is the same as the first task, appropriate exceptions are thrown.
     * If the first task is already a child of the second task, an error message is displayed.
     *
     * @param firstID   The ID of the first task.
     * @param second    The ID of the second task as a string.
     * @param firstTask The first task object.
     * @return The name of the second task.
     * @throws ListNotFoundException If the second task is not found.
     * @throws SameTaskException     If the first and second tasks are the same.
     * @throws TaskNotFoundException If the second task is not found or is deleted.
     */


    private String assignToTask(int firstID, String second, Task firstTask)
            throws ListNotFoundException, SameTaskException, TaskNotFoundException {
        int secondID;
        try {
            secondID = Integer.parseInt(second);
        } catch (NumberFormatException ignored) {
            throw new ListNotFoundException(second);
        }
        Optional<Task> secondTask = getTask(secondID);
        if (secondTask.isEmpty() || secondTask.get().isDeleted()) {
            throw new TaskNotFoundException(secondID);
        }
        if (firstID == secondID) {
            throw new SameTaskException();
        }
        if (getParent(secondID).equals(Optional.of(firstTask))) {
            System.err.println("ERROR: Parent can't be assigned to its child.");
            return null;
        }

        Optional<Task> parent = getParent(firstID);
        if (parent.isPresent() && !parent.get().isDeleted()) {
            if (parent.equals(secondTask)) {
                System.err.println("ERROR: Task with ID " + firstID + " is already assigned to task with ID " + secondID + ".");
                return null;
            }
            parent.get().removeFromSubtask(firstID);
        } else {
            tasks.remove(firstTask);
        }
        secondTask.get().addSubtask(firstTask);
        return secondTask.get().getName();
    }

    /**
     * Assigns the given task to the specified task list.
     * If the task is already present in the list,
     * a ListContainsTaskException is thrown.
     *
     * @param firstID   The ID of the task to be assigned.
     * @param firstTask The task object to be assigned.
     * @param list      The task list to which the task will be assigned.
     * @return The name of the task list.
     * @throws ListContainsTaskException If the task is already present in the list.
     */
    private String assignToTaskList(int firstID, Task firstTask, TaskList list) throws ListContainsTaskException {
        if (list.getTask(firstID).isPresent() || list.containsTask(firstID)) {
            throw new ListContainsTaskException(firstID);
        }
        list.getTasks().removeIf(subtask -> firstTask.getSubtask(subtask.getId()).isPresent());
        list.addTask(firstTask);
        return list.getName();
    }


    /**
     * Restores a deleted task.
     *
     * @param id The ID of the task to restore.
     * @return The restored task.
     * @throws TaskNotFoundException If the task with the given ID is not found.
     */

    public Task undoDelete(int id) throws TaskNotFoundException {
        Optional<Task> taskToRestore = getTask(id);
        if (taskToRestore.isPresent() && taskToRestore.get().isDeleted()) {
            Task restoredTask = taskToRestore.get();

            restoreTaskInTaskLists(restoredTask);

            Optional<Task> parentWhenDeleted = getParent(id);
            if (parentWhenDeleted.isPresent()) {
                handleParentTask(restoredTask, parentWhenDeleted.get());
            } else {
                restoreMainTask(restoredTask);
            }

            restoredTask.restore();
            return taskToRestore.get();
        } else {
            throw new TaskNotFoundException(id);
        }
    }

    /**
     * Restores the specified task in all task lists.
     * If the task is already present in a list,
     * it is removed and then re-added.
     * Additionally, any subtasks of the restored task that exist
     * in the task lists are removed.
     *
     * @param restoredTask The task to be restored in the task lists.
     */

    private void restoreTaskInTaskLists(Task restoredTask) {
        for (TaskList list : lists) {
            if (list.getTasks().contains(restoredTask)) {
                list.getTasks().remove(restoredTask);
                list.addTask(restoredTask);
            }
            list.getTasks().removeIf(subtask -> restoredTask.getSubtask(subtask.getId()).isPresent());
        }
    }

    /**
     * Handles the relationship between a restored task and its parent task at the time of deletion.
     * If the parent task was deleted, the restored task is added to any task list that contains the parent.
     * Additionally, the restored task is removed from the subtasks of the deleted parent and added back
     * to the main tasks list.
     * If the parent task was not deleted, the restored task is removed from its
     * current position in the parent's subtasks and then added back to the parent's subtasks.
     *
     * @param restoredTask      The task that has been restored.
     * @param parentWhenDeleted The parent task of the restored task at the time of its deletion.
     */

    private void handleParentTask(Task restoredTask, Task parentWhenDeleted) {
        if (parentWhenDeleted.isDeleted()) {
            for (TaskList list : lists) {
                if (list.containsTask(parentWhenDeleted.getId())) {
                    list.addTask(restoredTask);
                }
            }
            // If the parent is deleted, remove the task from its parent's subtasks
            parentWhenDeleted.removeFromSubtask(restoredTask.getId());
            // Add the task back to the main tasks list at the end
            tasks.add(restoredTask);
        } else {
            // If the parent is not deleted, remove the task from its current position
            parentWhenDeleted.removeFromSubtask(restoredTask.getId());
            // Add the task back to its parent's subtasks at the end
            parentWhenDeleted.addSubtask(restoredTask);
        }
    }

    /**
     * If the task is a main task and is deleted, then it will be removed and added to the tasks.
     *
     * @param restoredTask the task, which will be restored.
     */

    private void restoreMainTask(Task restoredTask) {
        // If the task is a main task, remove it from its current position
        tasks.remove(restoredTask);
        // Add it back to the main tasks list at the end
        tasks.add(restoredTask);
    }


    /**
     * Deletes a task.
     *
     * @param id The ID of the task to delete.
     */


    public void delete(int id) {
        Optional<Task> taskToDelete = getTask(id);
        // Existence Optional<Task> taskToDelete will be checked in the DeleteCommand.
        Task deletedTask = taskToDelete.get();
        // Mark the task as deleted
        deletedTask.delete();
    }

    /**
     * Sorts the tasks based on their priorities.
     */

    public void sort() {
        tasks.sort(Comparator
                .comparing(Task::getPriority, Comparator.nullsLast(Comparator.comparingInt(Priority::getPriority))));
        tasks.forEach(Task::sort);
    }
}
