package edu.kit.informatik.model;

import java.util.*;

/**
 * Represents a list of tasks.
 * Provides methods to manage, query, and sort tasks within the list.
 * Supports operations like adding tasks and setting tags.
 * Each task list is uniquely identified by its name.
 *
 * @author utobm
 * @version 1.0
 */

public class TaskList {

    private final List<Task> tasks = new ArrayList<>();

    private final List<String> tags = new ArrayList<>();

    private final String name;

    /**
     * Constructs a new task list with the specified name.
     *
     * @param name The name of the task list.
     */

    public TaskList(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this task list.
     *
     * @return The name of the task list.
     */


    public String getName() {
        return name;
    }

    /**
     * Adds a task to this task list.
     *
     * @param task The task to be added.
     */


    public void addTask(Task task) {
        if (!tasks.contains(task)) {
            this.tasks.add(task);
        }
    }


    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task.
     * @return An Optional containing the task if found, otherwise empty.
     */

    public Optional<Task> getTask(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return Optional.of(task);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the task list contains a task with the specified ID.
     * This method will also search within the subtasks of each task in the list.
     *
     * @param id The ID of the task to search for.
     * @return {@code true} if a task with the specified ID is found, {@code false} otherwise.
     */
    public boolean containsTask(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return true;
            }
            if (task.containsSubtask(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of tasks in this task list.
     *
     * @return The list of tasks.
     */

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds a tag to this task list.
     *
     * @param tag The tag to be added.
     * @return {@code true} if the tag was added successfully, {@code false} if the tag already exists.
     */


    public boolean setTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            return true;
        }
        return false;
    }

    /**
     * Sorts the tasks in this task list based on their priority.
     */


    public void sort() {

        tasks.sort(Comparator
                .comparing(Task::getPriority, Comparator.nullsLast(Comparator.comparingInt(Priority::getPriority))));
        for (Task task : tasks) {
            task.sort();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskList taskList = (TaskList) o;
        return Objects.equals(name, taskList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
