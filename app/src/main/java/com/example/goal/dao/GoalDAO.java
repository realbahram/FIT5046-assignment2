package com.example.goal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.goal.entity.Goal;

import java.util.List;

/**
 * DAO interface for the Goal entity
 */
@Dao
public interface GoalDAO {

    /**
     * Inserts a new goal into the database
     *
     * @param goal The goal to insert
     */
    @Insert
    void insertGoal(Goal goal);

    /**
     * Deletes a goal from the database
     *
     * @param goal The goal to delete
     */
    @Delete
    void deleteGoal(Goal goal);

    /**
     * Updates an existing goal in the database
     *
     * @param goal The goal to update
     */
    @Update
    void updateGoal(Goal goal);

    /**
     * Retrieves all goals as LiveData
     *
     * @return LiveData containing the list of goals
     */
    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> getAllGoals();

    /**
     * Retrieves all goals synchronously
     *
     * @return The list of goals
     */
    @Query("SELECT * FROM goal")
    List<Goal> getAllGoalsSync();
}

