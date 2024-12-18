package edu.kit.informatik.model;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a task with potential subtasks, tags, and other attributes.
 * Provides methods to manage, query, modify, and sort tasks and their subtasks.
 * Supports operations like toggling task status, deleting, restoring, and adding tags.
 * Each task is uniquely identified by its ID.
 *
 * @author utobm
 * @version 1.0
 */


public class Task {

    private final List<Task> subtasks = new ArrayList<>();


    private final List<String> tags = new ArrayList<>();

    private final String name;

    private Priority priority;

    private final int id;

    private LocalDate date;

    private boolean done = false;


    private boolean deleted = false;

    /**
     * Constructs a new task with the specified attributes.
     *
     * @param name     The name of the task.
     * @param priority The priority of the task.
     * @param date     The date associated with the task.
     * @param id       The unique identifier for the task.
     */


    public Task(String name, Priority priority, LocalDate date, int id) {
        this.name = name;
        this.priority = priority;
        this.date = date;
        this.id = id;
    }

    /**
     * Retrieves the name of the task.
     *
     * @return The name of the task.
     */

    public String getName() {
        return name;
    }

    /**
     * Retrieves the priority of the task.
     *
     * @return The priority of the task.
     */

    public Priority getPriority() {
        return priority;
    }

    /**
     * Retrieves the date associated with the task.
     *
     * @return The date of the task.
     */

    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the unique identifier of the task.
     *
     * @return The ID of the task.
     */

    public int getId() {
        return id;
    }

    /**
     * Retrieves the subtasks associated with this task.
     *
     * @return A list of subtasks.
     */

    public List<Task> getSubtasks() {
        return subtasks;
    }

    /**
     * Changes the date of the task.
     *
     * @param date The new date to set.
     */


    public void changeDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Updates the priority of the task.
     *
     * @param priority The new priority to set.
     */

    public void changePriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Retrieves the tags associated with the task.
     *
     * @return A list of tags.
     */

    public List<String> getTags() {
        return List.copyOf(tags);
    }

    /**
     * Adds a tag to the task if it doesn't already exist.
     *
     * @param tag The tag to add.
     * @return True if the tag was added, false otherwise.
     */

    public boolean setTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            return true;
        }
        return false;
    }

    /**
     * Checks if the current task contains a subtask with the specified ID.
     * This method will also search recursively within the subtasks of each subtask.
     *
     * @param id The ID of the subtask to search for.
     * @return {@code true} if a subtask with the specified ID is found, {@code false} otherwise.
     */

    public boolean containsSubtask(int id) {
        for (Task task : subtasks) {
            if (task.getId() == id || task.containsSubtask(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a subtask with the specified ID from the current task.
     * This method will also search recursively within the subtasks of each subtask.
     *
     * @param id The ID of the subtask to retrieve.
     * @return An {@code Optional} containing the subtask if found, or an empty {@code Optional} if not found.
     */

    public Optional<Task> getSubtask(int id) {
        for (Task task : subtasks) {
            if (task.getId() == id) {
                return Optional.of(task);
            }
            Optional<Task> subtask = task.getSubtask(id);
            if (subtask.isPresent()) {
                return subtask;
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the task is marked as done.
     *
     * @return True if the task is done, false otherwise.
     */


    public boolean isDone() {
        return done;
    }

    /**
     * Sets the status of the current task and its non-deleted subtasks.
     *
     * @param status The status to set for the task.
     *               If true, the task is marked as done;
     *               otherwise, it's marked as not done.
     */
    private void setStatus(boolean status) {
        done = status;
        for (Task subtask : subtasks) {
            if (!subtask.isDeleted()) {
                subtask.setStatus(status);
            }
        }
    }

    /**
     * Checks if the task is marked as deleted.
     *
     * @return True if the task is deleted, false otherwise.
     */

    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Toggles the status of the task between done and not done.
     */


    public void toggle() {
        setStatus(!done);
    }

    private void setAvailability(boolean status) {
        deleted = status;
        for (Task subtask : subtasks) {
            subtask.setAvailability(status);
        }
    }

    /**
     * Marks the task as deleted.
     */


    public void delete() {
        if (!deleted) {
            setAvailability(true);
        }
    }

    /**
     * Restores a previously deleted task.
     */

    public void restore() {
        if (deleted) {
            setAvailability(false);
        }
    }

    /**
     * Counts all subtasks associated with this task.
     *
     * @return The total number of subtasks.
     */


    public int countAllSubtasks() {
        int count = 0;
        for (Task task : subtasks) {
            if (!task.deleted) {
                count++;
            }
        }
        for (Task subtask : subtasks) {
            if (!subtask.deleted) {
                count += subtask.countAllSubtasks();
            }
        }
        return count;
    }

    /**
     * Adds a subtask to the current task.
     *
     * @param task The subtask to be added.
     */

    public void addSubtask(Task task) {
        subtasks.add(task);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return An optional containing the task if found, empty otherwise.
     */

    public Optional<Task> getTask(int id) {
        if (this.id == id) {
            return Optional.of(this);
        }
        for (Task task : subtasks) {
            Optional<Task> result = task.getTask(id);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves the parent of a task by its ID.
     *
     * @param id The ID of the task to retrieve its parent.
     * @return An optional containing the parent task if found, empty otherwise.
     */


    public Optional<Task> getParent(int id) {
        for (Task task : subtasks) {
            if (task.id == id) {
                return Optional.of(this);
            }
        }
        for (Task task : subtasks) {
            Optional<Task> result = task.getParent(id);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Removes a subtask from the current task by its ID.
     *
     * @param id The ID of the subtask to remove.
     * @return An optional containing the parent task if the subtask was removed, empty otherwise.
     */


    public Optional<Task> removeFromSubtask(int id) {
        Iterator<Task> iterator = subtasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.id == id) {
                iterator.remove();
                return Optional.of(this);
            }
            Optional<Task> potentialParent = task.removeFromSubtask(id);
            if (potentialParent.isPresent()) {
                return potentialParent;
            }
        }
        return Optional.empty();
    }

    /**
     * Sorts the subtasks based on their priority.
     */


    public void sort() {
        subtasks.sort(Comparator
                .comparing(Task::getPriority, Comparator.nullsLast(Comparator.comparingInt(Priority::getPriority))));
        for (Task subtask : subtasks) {
            subtask.sort();
        }
    }
}
