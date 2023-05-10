package com.example.goal.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Graph extends Fragment {

    private Graph binding;
    private FragmentGraphBinding addBinding;
    private GoalViewModel goalViewModel;
    private PieChart pieChart;

    public Graph() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = FragmentGraphBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        goalViewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        pieChart = view.findViewById(R.id.pieChart);
        updatePieChart();
        return view;
    }

    private void updatePieChart() {
        goalViewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
            @Override
            public void onChanged(List<Goal> goals) {
                if (goals != null && !goals.isEmpty()) {
                    // Calculate the completion status
                    int completedGoals = 0;
                    int totalGoals = goals.size();
                    for (Goal goal : goals) {
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
