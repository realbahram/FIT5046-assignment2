package com.example.goal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.goal.R;
import com.example.goal.entity.Goal;
import com.example.goal.recycler.RecyclerViewAdapter;
import com.example.goal.viewmodel.GoalViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that displays all goals associated with the logged-in user.
 */
public class AllGoals extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private GoalViewModel goalViewModel;
    private int loggedInUserId; // Assuming you have the loggedin user ID

    public AllGoals() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of the fragment.
     *
     * @return A new instance of the fragment.
     */
    public static AllGoals newInstance() {
        return new AllGoals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_goals, container, false);
        // Initializing the RecyclerView and GoalViewModel
        recyclerView = rootView.findViewById(R.id.recyclerView);
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        recyclerViewAdapter = new RecyclerViewAdapter(goalViewModel);

        // Setting up the RecyclerView with a layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Retrieve the logged in user ID from shared preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt("customerId", 0);

        // Observe the goals LiveData in the GoalViewModel
        goalViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                // Filter the goals based on the logged in user ID
                List<Goal> filteredGoals = filterGoalsByUserId(goals, loggedInUserId);

                // Update the RecyclerView adapter with the filtered list of goals
                recyclerViewAdapter.setGoals(filteredGoals);
            }
        });

        return rootView;
    }

    /**
     * Filters the list of goals based on the logged in user ID.
     *
     * @param goals  The list of goals to filter
     * @param userId The ID of the user to filter the goals by
     * @return The filtered list of goals associated with the logged user ID
     */
    private List<Goal> filterGoalsByUserId(List<Goal> goals, int userId) {
        List<Goal> filteredGoals = new ArrayList<>();
        for (Goal goal : goals) {
            if (goal.getCustomerId() == userId) {
                filteredGoals.add(goal);
            }
        }
        return filteredGoals;
    }
}


