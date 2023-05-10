package com.example.goal.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.PeriodicWorkRequest;

import com.example.goal.R;
import com.example.goal.UploadWorker;
import com.example.goal.databinding.FragmentGraphBinding;
import com.example.goal.databinding.HomeFragmentBinding;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;
import com.example.goal.repository.TheySaidSoAPI;
import com.example.goal.retrofit.RetrofitInterface;
import com.example.goal.viewmodel.GoalViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Graph extends Fragment {

    private FragmentGraphBinding binding;
    //private FragmentGraphBinding addBinding;
    private GoalViewModel goalViewModel;
    private PieChart pieChart;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();


    public Graph() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = FragmentGraphBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        goalViewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        pieChart = binding.pieChart;

        // Initialize the calendars
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Set up the start date picker
        binding.startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        // Set up the end date picker
        binding.endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });

        // Get the initial start and end dates
        long startDate = startDateCalendar.getTime().getTime();
        long endDate = endDateCalendar.getTime().getTime();

        // Update the pie chart with the initial dates
        updatePieChart(startDate, endDate);

        return view;
    }
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isStartDate) {
                    startDateCalendar.set(Calendar.YEAR, year);
                    startDateCalendar.set(Calendar.MONTH, monthOfYear);
                    startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String startDate = formatDate(startDateCalendar.getTime());
                    binding.startDateEditText.setText(startDate);
                } else {
                    endDateCalendar.set(Calendar.YEAR, year);
                    endDateCalendar.set(Calendar.MONTH, monthOfYear);
                    endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String endDate = formatDate(endDateCalendar.getTime());
                    binding.endDateEditText.setText(endDate);
                }

                // Update the pie chart with the selected dates
                updatePieChart(startDateCalendar.getTimeInMillis(), endDateCalendar.getTimeInMillis());
            }
        };
        int year, month, day;
        if (isStartDate) {
            year = startDateCalendar.get(Calendar.YEAR);
            month = startDateCalendar.get(Calendar.MONTH);
            day = startDateCalendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = endDateCalendar.get(Calendar.YEAR);
            month = endDateCalendar.get(Calendar.MONTH);
            day = endDateCalendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                dateSetListener,
                year,
                month,
                day
        );

        // Show the dialog
        datePickerDialog.show();
    }




    private void updatePieChart(long startDate, long endDate) {
        goalViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                if (goals != null && !goals.isEmpty()) {
                    // Filter the goals data by the selected dates
                    List<Goal> filteredGoals = new ArrayList<>();
                    for (Goal goal : goals) {
                        try {
                            Date goalStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(goal.getStartDate());
                            Date goalEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(goal.getEndDate());
                            if (goalStartDate.getTime() >= startDate && goalEndDate.getTime() <= endDate) {
                                filteredGoals.add(goal);
                            }
                        } catch (ParseException e) {
                            // Handle parsing error
                        }
                    }

                    // Calculate the completion status
                    int completedGoals = 0;
                    int totalGoals = filteredGoals.size();
                    for (Goal goal : filteredGoals) {
                        if (goal.getStatus().equals("Complete")) {
                            completedGoals++;
                        }
                    }
                    int incompleteGoals = totalGoals - completedGoals;

                    // Update the pie chart
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry((float) completedGoals, "Complete"));
                    entries.add(new PieEntry((float) incompleteGoals, "Incomplete"));

                    PieDataSet dataSet = new PieDataSet(entries, "Goal Completion");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setDrawValues(true);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(12f);
                    data.setValueTextColor(Color.WHITE);

                    pieChart.setData(data);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleColor(Color.TRANSPARENT);
                    pieChart.setTransparentCircleRadius(0f);
                    pieChart.setDrawEntryLabels(false);
                    pieChart.getLegend().setEnabled(false);
                    pieChart.animateY(1000);

                    // Refresh the chart
                    pieChart.invalidate();

                    // Hide the center text if goals are available
                    pieChart.setCenterText("");
                } else {
                    // Clear the chart data and set a message
                    pieChart.clear();
                    pieChart.setNoDataText("Set a goal NOW!");
                    pieChart.setNoDataTextColor(Color.GRAY);
                }
            }
        });
    }
}
