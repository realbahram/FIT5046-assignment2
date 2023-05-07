package com.example.goal.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.goal.entity.Goal;

import java.util.List;

@Dao
public interface GoalDAO {
    @Insert
    void insertGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("SELECT * FROM goal")
    LiveData<List<Goal>> getAllGoals();
}