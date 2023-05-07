package com.example.goal.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goal.R;
import com.example.goal.entity.Goal;
import com.example.goal.repository.GoalRepository;
import com.example.goal.viewmodel.GoalViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGoalFragment extends Fragment {

    private GoalViewModel goalViewModel;
    private TextView addedGoalTextView;

    public AddGoalFragment() {
        // Required empty public constructor
    }

    public static AddGoalFragment newInstance() {
        return new AddGoalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_goal, container, false);
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        // Retrieve references to UI elements
        Spinner categorySpinner = rootView.findViewById(R.id.spinner3);
        EditText nameEditText = rootView.findViewById(R.id.editTextName);
        EditText tasksEditText = rootView.findViewById(R.id.editTextTasks);
        EditText notesEditText = rootView.findViewById(R.id.editTextNotes);
        EditText startDateEditText = rootView.findViewById(R.id.editTextStartDate);
        EditText endDateEditText = rootView.findViewById(R.id.editTextEndDate);
        RadioGroup priorityRadioGroup = rootView.findViewById(R.id.priority_radiogroup);
        Button addButton = rootView.findViewById(R.id.button4);
        addedGoalTextView = rootView.findViewById(R.id.added_goal_textview);

        // Set up the observer to listen for the goal insertion result
        final Observer<Boolean> goalInsertionObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Log.d("GoalViewModel", "Goal inserted successfully");
                    Toast.makeText(getActivity(), "Goal added successfully", Toast.LENGTH_SHORT).show();
                    addedGoalTextView.setText(nameEditText.getText().toString()); // Display the added goal's name
                    // Clear the input fields after successful insertion
                    nameEditText.setText("");
                    tasksEditText.setText("");
                    notesEditText.setText("");
                    startDateEditText.setText("");
                    endDateEditText.setText("");
                    priorityRadioGroup.clearCheck();
                } else {
                    Log.d("GoalViewModel", "Failed to insert goal");
                    Toast.makeText(getActivity(), "Failed to add goal", Toast.LENGTH_SHORT).show();
                }
            }
        };

        goalViewModel.getGoalInsertionLiveData().observe(getViewLifecycleOwner(), goalInsertionObserver);

        // Set a click listener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user inputs
                String category = categorySpinner.getSelectedItem().toString();
                String name = nameEditText.getText().toString();
                String tasks = tasksEditText.getText().toString();
                String notes = notesEditText.getText().toString();
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();

                // Determine the selected priority
                int priorityId = priorityRadioGroup.getCheckedRadioButtonId();
                String priority;
                if (priorityId == R.id.high_priority_radiobutton) {
                    priority = "High";
                } else if (priorityId == R.id.medium_priority_radiobutton) {
                    priority = "Medium";
                } else if (priorityId == R.id.low_priority_radiobutton) {
                    priority = "Low";
                } else {
                    // Default to empty string if no priority is selected
                    priority = "";
                }

                // Create a new Goal object
                Goal goal = new Goal(category, name, tasks, notes, startDate, endDate, priority);

                // Insert the goal using the GoalViewModel
                goalViewModel.insertGoal(goal);

                getActivity().onBackPressed();
            }
        });

        // Observe the goals LiveData in the GoalViewModel
        goalViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                // Handle the updated list of goals
                // You can perform any necessary operations with the goals here
                // For example, you can update a RecyclerView or display the goals in a TextView

                // Example: Update a RecyclerView with the list of goals
                //goalAdapter.setGoals(goals);
                //goalAdapter.notifyDataSetChanged();

                // Example: Display the goals in a TextView
                StringBuilder stringBuilder = new StringBuilder();
                for (Goal goal : goals) {
                    stringBuilder.append(goal.getName()).append("\n");
                }
                addedGoalTextView.setText(stringBuilder.toString());
            }
        });
        return rootView;
    }
}