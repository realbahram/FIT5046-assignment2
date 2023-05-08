package com.example.goal.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.goal.dao.GoalDAO;
import com.example.goal.database.GoalDatabase;
import com.example.goal.entity.Goal;

import java.util.List;
public class GoalRepository {
    private GoalDAO goalDao;
    private LiveData<List<Goal>> goalsLiveData;
    private MutableLiveData<Boolean> goalInsertionLiveData;

    public GoalRepository(Application application) {
        GoalDatabase database = GoalDatabase.getInstance(application);
        goalDao = database.goalDao();
        goalsLiveData = goalDao.getAllGoals();
        goalInsertionLiveData = new MutableLiveData<>();
    }

    public void insertGoal(Goal goal, InsertionCallback callback) {
        new InsertGoalAsyncTask(goalDao, callback).execute(goal);
    }

    public void updateGoalStatus(Goal goal, UpdateStatusCallback callback) {
        new UpdateGoalStatusAsyncTask(goalDao, callback).execute(goal);
    }

    public void deleteGoal(Goal goal, DeletionCallback callback) {
        new DeleteGoalAsyncTask(goalDao, callback).execute(goal);
    }

    public LiveData<List<Goal>> getGoals() {
        return goalsLiveData;
    }

    public LiveData<Boolean> getGoalInsertionLiveData() {
        return goalInsertionLiveData;
    }

    private static class InsertGoalAsyncTask extends AsyncTask<Goal, Void, Boolean> {
        private GoalDAO goalDao;
        private InsertionCallback callback;

        public InsertGoalAsyncTask(GoalDAO goalDao, InsertionCallback callback) {
            this.goalDao = goalDao;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Goal... goals) {
            try {
                goalDao.insertGoal(goals[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (callback != null) {
                callback.onInsertionComplete(isSuccess);
            }
        }
    }

    private static class DeleteGoalAsyncTask extends AsyncTask<Goal, Void, Boolean> {
        private GoalDAO goalDao;
        private DeletionCallback callback;

        public DeleteGoalAsyncTask(GoalDAO goalDao, DeletionCallback callback) {
            this.goalDao = goalDao;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Goal... goals) {
            try {
                goalDao.deleteGoal(goals[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (callback != null) {
                callback.onDeletionComplete(isSuccess);
            }
        }
    }

    public interface InsertionCallback {
        void onInsertionComplete(boolean isSuccess);
    }

    public interface DeletionCallback {
        void onDeletionComplete(boolean isSuccess);
    }


    private static class UpdateGoalStatusAsyncTask extends AsyncTask<Goal, Void, Boolean> {
        private GoalDAO goalDao;
        private UpdateStatusCallback callback;

        public UpdateGoalStatusAsyncTask(GoalDAO goalDao, UpdateStatusCallback callback) {
            this.goalDao = goalDao;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Goal... goals) {
            try {
                goals[0].setStatus("Complete"); // Change the status to "Complete"
                goalDao.updateGoal(goals[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (callback != null) {
                callback.onStatusUpdateComplete(isSuccess);
            }
        }
    }

    public interface UpdateStatusCallback {
        void onStatusUpdateComplete(boolean isSuccess);
    }
}