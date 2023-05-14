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
/**
 * Database class that represents the Room database for the Goal app
 */
@Database(entities = {Customer.class, Goal.class}, version = 4)
public abstract class GoalDatabase extends RoomDatabase {

    private static GoalDatabase instance;

    /**
     * DAO for the Goal entity
     *
     * @return The GoalDAO instance
     */
    public abstract GoalDAO goalDao();

    private static final int NUMBER_OF_THREADS = 5;

    /**
     * Executor service for database operations on a separate thread
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Returns the instance of the GoalDatabase
     *
     * @param context The application context
     * @return The GoalDatabase instance
     */
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

