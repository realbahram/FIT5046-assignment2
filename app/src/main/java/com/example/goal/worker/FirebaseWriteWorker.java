package com.example.goal.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.goal.dao.CustomerDAO;
import com.example.goal.dao.GoalDAO;
import com.example.goal.database.CustomerDatabase;
import com.example.goal.database.GoalDatabase;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Date;
import java.util.List;

public class FirebaseWriteWorker extends Worker {
    private static final String TAG = "FirebaseWriteWorker";
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    public FirebaseWriteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Retrieve goal data from the Room database
            GoalDAO goalDao = GoalDatabase.getInstance(getApplicationContext()).goalDao();
            List<Goal> goals = goalDao.getAllGoalsSync();

            // Initialize Firebase Realtime Database reference
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("Goal");

            // Write goal data to Firebase
            for (Goal goal : goals) {
                String goalId = String.valueOf(goal.getId());
                reference.child(goalId).setValue(goal);
            }


            // Logging message with timestamp
            Log.d(TAG, "doWork() - Goal data written to Firebase at " + new Date().toString());

            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error writing goal data to Firebase: " + e.getMessage());
            return Result.failure();
        }
    }
}