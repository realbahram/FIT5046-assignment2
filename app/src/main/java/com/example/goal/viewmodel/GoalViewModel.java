package com.example.goal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.goal.entity.Goal;
import com.example.goal.repository.GoalRepository;

import java.util.List;

/**
 * ViewModel class for managing goal data and communication with the repository.
 */
public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;
    private MutableLiveData<Boolean> goalInsertionLiveData;

    /**
     * Constructor for GoalViewModel.
     *
     * @param application The application context.
     */
    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
        allGoals = goalRepository.getGoals();
        goalInsertionLiveData = new MutableLiveData<>();
    }

    /**
     * Retrieves all goals as LiveData.
     *
     * @return LiveData containing the list of all goals.
     */
    public LiveData<List<Goal>> getGoals() {
        return allGoals;
    }

    /**
     * Retrieves the LiveData indicating the success of goal insertion.
     *
     * @return LiveData holding the insertion status.
     */
    public LiveData<Boolean> getGoalInsertionLiveData() {
        return goalInsertionLiveData;
    }

    /**
     * Inserts a new goal.
     *
     * @param goal The goal object to insert.
     */
    public void insertGoal(Goal goal) {
        goalRepository.insertGoal(goal, new GoalRepository.InsertionCallback() {
            @Override
            public void onInsertionComplete(boolean isSuccess) {
                goalInsertionLiveData.setValue(isSuccess);
            }
        });
    }

    /**
     * Deletes a goal.
     *
     * @param goal The goal object to delete.
     */
    public void deleteGoal(Goal goal) {
        goalRepository.deleteGoal(goal, new GoalRepository.DeletionCallback() {
            @Override
            public void onDeletionComplete(boolean isSuccess) {
                // Handle the completion of the deletion
            }
        });
    }

    /**
     * Updates the status of a goal.
     *
     * @param goal The goal object to update the status for.
     */
    public void updateGoalStatus(Goal goal) {
        goalRepository.updateGoalStatus(goal, new GoalRepository.UpdateStatusCallback() {
            @Override
            public void onStatusUpdateComplete(boolean isSuccess) {
                // Handle the completion of the status update
            }
        });
    }

    /**
     * Retrieves all goals synchronously.
     *
     * @return The list of all goals.
     */
    public List<Goal> getGoalsSync() {
        try {
            return goalRepository.getGoalsSync(new GoalRepository.GetGoalsSyncCallback() {
                @Override
                public void onGetGoalsSyncComplete(List<Goal> goals) {
                    // Handle the completion of the synchronous goals retrieval
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


