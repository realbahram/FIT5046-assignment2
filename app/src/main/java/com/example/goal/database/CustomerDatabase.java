package com.example.goal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.goal.dao.CustomerDAO;
import com.example.goal.entity.Customer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Customer.class}, version = 3, exportSchema = false)
public abstract class CustomerDatabase extends RoomDatabase {
    public abstract CustomerDAO customerDao();
    private static CustomerDatabase INSTANCE;
    //we create an ExecutorService with a fixed thread pool so we can later run
    //database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //A synchronized method in a multi threaded environment means that two threads
    //are not allowed to access data at the same time
    public static synchronized CustomerDatabase getInstance(final Context
                                                                    context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CustomerDatabase.class, "CustomerDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
