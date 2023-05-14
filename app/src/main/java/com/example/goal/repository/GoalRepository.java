package com.example.goal.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.goal.dao.GoalDAO;
import com.example.goal.database.GoalDatabase;
import com.example.goal.entity.Goal;

import java.util.List;
/**
 * The GoalRepository class is responsible for managing the goals data and handling database operations related to goals.
 */
public class GoalRepository {
    private GoalDAO goalDao;
    private LiveData<List<Goal>> goalsLiveData;
    private MutableLiveData<Boolean> goalInsertionLiveData;
    /**
     * Constructs a new GoalRepository object.
     *
     * @param application The application context.
     */
    public GoalRepository(Application application) {
        GoalDatabase database = GoalDatabase.getInstance(application);
        goalDao = database.goalDao();
        goalsLiveData = goalDao.getAllGoals();
        goalInsertionLiveData = new MutableLiveData<>();
    }
    /**
     * Inserts a new goal into the database.
     *
     * @param goal     The goal to be inserted.
     * @param callback The callback to be invoked when the insertion is complete.
     */
    public void insertGoal(Goal goal, InsertionCallback callback) {
        new InsertGoalAsyncTask(goalDao, callback).execute(goal);
    }
    /**
     * Updates the status of a goal in the database.
     *
     * @param goal     The goal to be updated.
     * @param callback The callback to be invoked when the status update is complete.
     */
    public void updateGoalStatus(Goal goal, UpdateStatusCallback callback) {
        new UpdateGoalStatusAsyncTask(goalDao, callback).execute(goal);
    }
    /**
     * Deletes a goal from the database.
     *
     * @param goal     The goal to be deleted.
     * @param callback The callback to be invoked when the deletion is complete.
     */
    public void deleteGoal(Goal goal, DeletionCallback callback) {
        new DeleteGoalAsyncTask(goalDao, callback).execute(goal);
    }
    /**
     * Retrieves all goals from the database as LiveData.
     *
     * @return LiveData containing a list of goals.
     */
    public LiveData<List<Goal>> getGoals() {
        return goalsLiveData;
    }
    /**
     * Retrieves the LiveData object for goal insertion status.
     *
     * @return LiveData object indicating the success of goal insertion.
     */
    public LiveData<Boolean> getGoalInsertionLiveData() {
        return goalInsertionLiveData;
    }
    /**
     * Retrieves all goals from the database synchronously.
     *
     * @param callback The callback to be invoked when the synchronous retrieval is complete.
     * @return List of goals retrieved from the database.
     */
    public List<Goal> getGoalsSync(GetGoalsSyncCallback callback) {
        try {
            return new GetGoalsSyncAsyncTask(goalDao, callback).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * AsyncTask for inserting a goal into the database.
     */
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
    /**
     * AsyncTask for deleting a goal from the database.
     */
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
    /**
     * Callback interface for goal insertion completion.
     */
    public interface InsertionCallback {
        /**
         * Called when the goal insertion is complete.
         *
         * @param isSuccess Indicates whether the insertion was successful or not.
         */
        void onInsertionComplete(boolean isSuccess);
    }
    /**
     * Callback interface for goal deletion completion.
     */
    public interface DeletionCallback {
        /**
         * Called when the goal deletion is complete.
         *
         * @param isSuccess Indicates whether the deletion was successful or not.
         */
        void onDeletionComplete(boolean isSuccess);
    }

    /**
     * AsyncTask for updating the status of a goal in the database.
     */
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
    /**
     * Callback interface for goal status update completion.
     */
    public interface UpdateStatusCallback {
        /**
         * Called when the goal status update is complete.
         *
         * @param isSuccess Indicates whether the status update was successful or not.
         */
        void onStatusUpdateComplete(boolean isSuccess);
    }
    /**
     * AsyncTask for retrieving all goals from the database synchronously.
     */
    private static class GetGoalsSyncAsyncTask extends AsyncTask<Void, Void, List<Goal>> {
        private GoalDAO goalDao;
        private GetGoalsSyncCallback callback;

        public GetGoalsSyncAsyncTask(GoalDAO goalDao, GetGoalsSyncCallback callback) {
            this.goalDao = goalDao;
            this.callback = callback;
        }

        @Override
        protected List<Goal> doInBackground(Void... voids) {
            return goalDao.getAllGoalsSync();
        }

        @Override
        protected void onPostExecute(List<Goal> goals) {
            if (callback != null) {
                callback.onGetGoalsSyncComplete(goals);
            }
        }
    }

    /**
     * Callback interface for synchronous retrieval of goals.
     */
    public interface GetGoalsSyncCallback {
        /**
         * Called when the synchronous retrieval of goals is complete.
         *
         * @param goals The list of goals retrieved from the database.
         */
        void onGetGoalsSyncComplete(List<Goal> goals);
    }
}

