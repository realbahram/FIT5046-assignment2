package com.example.goal.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goal.R;
import com.example.goal.dao.GoalDAO;
import com.example.goal.entity.Goal;
import com.example.goal.viewmodel.GoalViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGoalFragment extends Fragment {

    private GoalViewModel goalViewModel;
    private TextView addedGoalTextView;
    private GoalDAO goalDao;
    private int userId;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private DatePickerDialog picker;

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
        //SharedPreferences sharedPreferences = ((LoginActivity) requireActivity()).getMySharedPreferences();
        goalViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        //SharedPreferences sharedPreferences = ((LoginActivity) requireActivity()).getMySharedPreferences();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve references to UI elements
        Spinner categorySpinner = rootView.findViewById(R.id.spinner3);
        TextInputLayout nameEditText = rootView.findViewById(R.id.editTextName);
        TextInputLayout tasksEditText = rootView.findViewById(R.id.editTextTasks);
        TextInputLayout notesEditText = rootView.findViewById(R.id.editTextNotes);
        EditText startDateEditText = rootView.findViewById(R.id.editTextStartDate);
        EditText endDateEditText = rootView.findViewById(R.id.editTextEndDate);
        RadioGroup priorityRadioGroup = rootView.findViewById(R.id.priority_radiogroup);
        Button addButton = rootView.findViewById(R.id.button4);
        addedGoalTextView = rootView.findViewById(R.id.added_goal_textview);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startDateEditText.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }

        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        endDateEditText.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });
        // Set up the observer to listen for the goal insertion result
        final Observer<Boolean> goalInsertionObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Log.d("GoalViewModel", "Goal inserted successfully");
                    Toast.makeText(getActivity(), "Goal added successfully", Toast.LENGTH_SHORT).show();
                    addedGoalTextView.setText(nameEditText.getEditText().getText().toString()); // Display the added goal's name
                    // Clear the input fields after successful insertion
                    nameEditText.getEditText().setText("");
                    tasksEditText.getEditText().setText("");
                    notesEditText.getEditText().setText("");
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int flag = 0;
                // Retrieve user inputs
                String category = categorySpinner.getSelectedItem().toString();
                String name = nameEditText.getEditText().getText().toString();
                String tasks = tasksEditText.getEditText().getText().toString();
                String notes = notesEditText.getEditText().getText().toString();
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String status = "Incomplete";
                if (TextUtils.isEmpty(name)) {
                    nameEditText.setError("Name cant be empty");
                    nameEditText.requestFocus();
                    flag = 1;
                }
                if (TextUtils.isEmpty(tasks)) {
                    tasksEditText.setError("cant be empty");
                    tasksEditText.requestFocus();
                    flag = 1;
                }
                if (category.equals("Select a category")) {
                    Toast.makeText(getActivity(), "Select a category", Toast.LENGTH_SHORT).show();
                    categorySpinner.requestFocus();
                    flag = 1;
                }
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
                    Toast.makeText(getActivity(), "select priority", Toast.LENGTH_SHORT).show();
                    // Default to empty string if no priority is selected
                    priorityRadioGroup.requestFocus();
                    flag = 1;
                    priority = "";
                }
                if(!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                    Date start;
                    Date end;
                    SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        start = f.parse(startDate);
                        end = f.parse(endDate);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (start.compareTo(end) > 0) {
                        Toast.makeText(getActivity(), "End date should be after the start date", Toast.LENGTH_SHORT).show();
                        startDateEditText.requestFocus();
                        endDateEditText.requestFocus();
                        flag = 1;
                    }
                }else {
                    Toast.makeText(getActivity(), "Dates cant be empty", Toast.LENGTH_SHORT).show();
                    startDateEditText.requestFocus();
                    endDateEditText.requestFocus();
                    flag =1;
                }
                if (flag == 0) {
                    // Create a new Goal object

                    int goalId = generateGoalId();
                    //int userId = sharedPref.getInt("customerId", userViewModel.getUserId());
                    //int userId = userViewModel.getUserId().getValue();
                    //Log.d("MyTagtttt", "User ID: " + id);
                    int customerId = sharedPreferences.getInt("customerId", 0);
                    Goal goal = new Goal(goalId, category, name, tasks, notes, startDate, endDate, priority, status, customerId);

                    // Insert the goal using the GoalViewModel
                    goalViewModel.insertGoal(goal);
                    /// get the current customer
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference("User");
//                reference.child(firebaseuser.getUid()).setValue(goal);
                    getActivity().onBackPressed();
                }
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

    private int generateGoalId() {
        int goalId;
        goalId = new Random().nextInt(90000) + 10000;
        return goalId;
    }


}