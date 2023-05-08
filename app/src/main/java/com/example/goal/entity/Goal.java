package com.example.goal.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
//@Entity(tableName = "goal",
        //foreignKeys = @ForeignKey(entity = Customer.class,
                //parentColumns = "id",
                //childColumns = "uid",
                //onDelete = ForeignKey.CASCADE))
@Entity(tableName = "goal")
public class Goal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tasks")
    private String tasks;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "startDate")
    private String startDate;

    @ColumnInfo(name = "endDate")
    private String endDate;

    @ColumnInfo(name = "priority")
    private String priority;

    @ColumnInfo(name = "status")
    private String status;

    // ... constructors, getters, and setters

    public Goal( String category, String name, String tasks, String notes,
                String startDate, String endDate, String priority, String status) {
        this.category = category;
        this.name = name;
        this.tasks = tasks;
        this.notes = notes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}





