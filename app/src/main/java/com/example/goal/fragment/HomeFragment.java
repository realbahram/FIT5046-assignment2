package com.example.goal.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.goal.R;
import com.example.goal.databinding.HomeFragmentBinding;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;
import com.example.goal.viewmodel.CustomerViewModel;
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

public class HomeFragment extends Fragment {
    private HomeFragmentBinding addBinding;
    private FirebaseAuth authProfile;
    private TextView textViewWelcome;
    private String name, email, address;
    private CustomerViewModel customerViewModel;
    private PieChart pieChart;
    private GoalViewModel goalViewModel;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        authProfile = FirebaseAuth.getInstance();
        textViewWelcome = view.findViewById(R.id.welcome_message_textview);
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        goalViewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        pieChart = view.findViewById(R.id.pieChart);

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "not Found the user information", Toast.LENGTH_SHORT).show();
        } else {
            showUserProfile(firebaseUser);
            updatePieChart();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String useId = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");
        referenceProfile.child(useId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
                Customer customer = snapshot.getValue(Customer.class);
                if (customer != null) {
                    name = customer.getName();
                    email = customer.getEmail();
                    address = customer.getAddress();
                    // save in local database
                    //customerViewModel.insert(temp);

                    textViewWelcome.setText("Welcome " + name + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something Gone wrong", Toast.LENGTH_SHORT).show();
            }
        });
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








