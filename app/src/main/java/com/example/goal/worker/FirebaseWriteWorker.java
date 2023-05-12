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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            // Delete goal data from Firebase that is not present in Room
            Set<String> existingGoalIds = new HashSet<>();
            for (Goal goal : goals) {
                existingGoalIds.add(String.valueOf(goal.getId()));
            }
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String goalId = childSnapshot.getKey();
                        if (!existingGoalIds.contains(goalId)) {
                            reference.child(goalId).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error deleting goal data from Firebase: " + error.getMessage());
                }
            });

            // Logging message with timestamp
            Log.d(TAG, "doWork() - Goal data synced with Firebase at " + new Date().toString());

            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error syncing goal data with Firebase: " + e.getMessage());
            return Result.failure();
        }
    }
}