package com.example.goal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.goal.dao.GoalDAO;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Customer.class, Goal.class}, version = 4)
public abstract class GoalDatabase extends RoomDatabase {
    private static GoalDatabase instance;

    public abstract GoalDAO goalDao();

    private static final int NUMBER_OF_THREADS = 10;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized GoalDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            GoalDatabase.class, "goal_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}