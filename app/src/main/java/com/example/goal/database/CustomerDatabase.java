package com.example.goal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.goal.dao.CustomerDAO;
import com.example.goal.entity.Customer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Database class that represents the Room database for the Customer entity
 */

@Database(entities = {Customer.class}, version = 3, exportSchema = false)
public abstract class CustomerDatabase extends RoomDatabase {

    public abstract CustomerDAO customerDao();

    private static CustomerDatabase INSTANCE;

    // ExecutorService with a fixed thread to run database operations asynchronously
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Returns the instance of the CustomerDatabase
     *
     * @param context The application context
     * @return The CustomerDatabase instance
     */
    public static synchronized CustomerDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CustomerDatabase.class, "CustomerDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}

