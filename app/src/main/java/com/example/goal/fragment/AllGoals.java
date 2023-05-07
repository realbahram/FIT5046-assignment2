package com.example.goal.fragment;

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

import java.util.List;


public class AllGoals extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private GoalViewModel goalViewModel;

    public AllGoals() {
        // Required empty public constructor
    }

    public static AllGoals newInstance() {
        return new AllGoals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_goals, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        recyclerViewAdapter = new RecyclerViewAdapter(goalViewModel);

        // Set up the RecyclerView with a layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Initialize the GoalViewModel
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        // Observe the goals LiveData in the GoalViewModel
        goalViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                // Update the RecyclerView adapter with the new list of goals
                recyclerViewAdapter.setGoals(goals);
            }
        });

        return rootView;
    }
}