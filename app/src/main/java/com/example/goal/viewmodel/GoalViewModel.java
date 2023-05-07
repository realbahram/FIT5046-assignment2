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
}