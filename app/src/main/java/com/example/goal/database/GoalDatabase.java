package com.example.goal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.goal.dao.GoalDAO;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;
@Database(entities = {Customer.class, Goal.class}, version = 2)
public abstract class GoalDatabase extends RoomDatabase {
    private static GoalDatabase instance;

    public abstract GoalDAO goalDao();

    public static synchronized GoalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            GoalDatabase.class, "goal_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}