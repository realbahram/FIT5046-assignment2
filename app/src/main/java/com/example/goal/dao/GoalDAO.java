package com.example.goal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.goal.entity.Goal;

import java.util.List;

@Dao
public interface GoalDAO {
    @Insert
    void insertGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);
    @Update
    void updateGoal(Goal goal);
    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> getAllGoals();
    @Query("SELECT * FROM goal")
    List<Goal> getAllGoalsSync();

}