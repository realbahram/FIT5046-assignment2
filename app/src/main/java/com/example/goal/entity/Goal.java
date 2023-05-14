package com.example.goal.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
/**
 * Represents a goal in the application.
 */
@Entity(tableName = "goal")
public class Goal {

    /**
     * The unique identifier for the goal.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The category of the goal.
     */
    @ColumnInfo(name = "category")
    private String category;

    /**
     * The name of the goal.
     */
    @ColumnInfo(name = "name")
    private String name;

    /**
     * The tasks associated with the goal.
     */
    @ColumnInfo(name = "tasks")
    private String tasks;

    /**
     * The notes for the goal.
     */
    @ColumnInfo(name = "notes")
    private String notes;

    /**
     * The start date of the goal.
     */
    @ColumnInfo(name = "startDate")
    private String startDate;

    /**
     * The end date of the goal.
     */
    @ColumnInfo(name = "endDate")
    private String endDate;

    /**
     * The priority level of the goal.
     */
    @ColumnInfo(name = "priority")
    private String priority;

    /**
     * The status of the goal.
     */
    @ColumnInfo(name = "status")
    private String status;

    /**
     * The customer ID associated with the goal.
     */
    @ColumnInfo(name = "customerId")
    private int customerId;

    /**
     * Default constructor for the Goal class.
     */
    public Goal() {
        // Default constructor required by Room.
    }

    /**
     * Constructor for creating a new Goal instance.
     *
     * @param id         The unique identifier for the goal.
     * @param category   The category of the goal.
     * @param name       The name of the goal.
     * @param tasks      The tasks associated with the goal.
     * @param notes      The notes for the goal.
     * @param startDate  The start date of the goal.
     * @param endDate    The end date of the goal.
     * @param priority   The priority level of the goal.
     * @param status     The status of the goal.
     * @param customerId The customer ID associated with the goal.
     */
    public Goal(int id, String category, String name, String tasks, String notes,
                String startDate, String endDate, String priority, String status, int customerId) {
        this.id = id;
        this.customerId = customerId;
        this.category = category;
        this.name = name;
        this.tasks = tasks;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.status = status;
    }

    /**
     * Retrieves the unique identifier of the goal.
     *
     * @return The unique identifier of the goal.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the goal.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the category of the goal.
     *
     * @return The category of the goal.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the goal.
     *
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Retrieves the name of the goal.
     *
     * @return The name of the goal.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the goal.
     *
     * @param name The name to set.
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the tasks associated with the goal.
     *
     * @return The tasks associated with the goal.
     */
    public String getTasks() {
        return tasks;
    }

    /**
     * Sets the tasks associated with the goal.
     *
     * @param tasks The tasks to set.
     */
    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    /**
     * Retrieves the notes for the goal.
     *
     * @return The notes for the goal.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes for the goal.
     *
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Retrieves the start date of the goal.
     *
     * @return The start date of the goal.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the goal.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date of the goal.
     *
     * @return The end date of the goal.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the goal.
     *
     * @param endDate The end date to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Retrieves the priority level of the goal.
     *
     * @return The priority level of the goal.
     */
    public String getPriority() {
        return priority;
    }



    /**
     * Sets the priority level of the goal.
     *
     * @param priority The priority level to set.
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Retrieves the status of the goal.
     *
     * @return The status of the goal.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the goal.
     *
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Retrieves the customer ID associated with the goal.
     *
     * @return The customer ID associated with the goal.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID associated with the goal.
     *
     * @param customerId The customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns a string representation of the goal object.
     *
     * @return The string representation of the goal object.
     */
    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", tasks='" + tasks + '\'' +
                ", notes='" + notes + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}

