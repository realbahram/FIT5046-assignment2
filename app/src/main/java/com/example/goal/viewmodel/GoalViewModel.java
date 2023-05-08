package com.example.goal.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.goal.entity.Goal;
import com.example.goal.repository.GoalRepository;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {
    private GoalRepository goalRepository;
    private LiveData<List<Goal>> allGoals;
    private MutableLiveData<Boolean> goalInsertionLiveData;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
        allGoals = goalRepository.getGoals();
        goalInsertionLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Goal>> getGoals() {
        return allGoals;
    }

    public LiveData<Boolean> getGoalInsertionLiveData() {
        return goalInsertionLiveData;
    }

    public void insertGoal(Goal goal) {
        goalRepository.insertGoal(goal, new GoalRepository.InsertionCallback() {
            @Override
            public void onInsertionComplete(boolean isSuccess) {
                goalInsertionLiveData.setValue(isSuccess);
            }
        });
    }

    public void deleteGoal(Goal goal) {
        goalRepository.deleteGoal(goal, new GoalRepository.DeletionCallback() {
            @Override
            public void onDeletionComplete(boolean isSuccess) {
                //body
            }
        });
    }

    public void updateGoalStatus(Goal goal) {
        goalRepository.updateGoalStatus(goal, new GoalRepository.UpdateStatusCallback() {
            @Override
            public void onStatusUpdateComplete(boolean isSuccess) {
                // Handle the completion of the status update
            }
        });
    }
    public List<Goal> getGoalsSync() {
        try {
            return goalRepository.getGoalsSync(new GoalRepository.GetGoalsSyncCallback() {
                @Override
                public void onGetGoalsSyncComplete(List<Goal> goals) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}