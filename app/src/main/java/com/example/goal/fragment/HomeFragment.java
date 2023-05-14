package com.example.goal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.goal.R;
import com.example.goal.UploadWorker;
import com.example.goal.databinding.HomeFragmentBinding;
import com.example.goal.entity.Customer;
import com.example.goal.entity.Goal;
import com.example.goal.repository.NinjasQuote;
import com.example.goal.repository.TheySaidSoAPI;
import com.example.goal.retrofit.RetrofitInterface;
import com.example.goal.viewmodel.CustomerViewModel;
import com.example.goal.viewmodel.GoalViewModel;
import com.example.goal.worker.FirebaseWriteWorker;
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
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * The HomeFragment class represents a fragment that displays the home screen of the Goal app.
 * It shows a welcome message, a quote fetched from an API, and provides a button to trigger a WorkManager job.
 * Additionally, it retrieves and displays the customer's name from shared preferences.
 */
public class HomeFragment extends Fragment {
    private HomeFragmentBinding addBinding;
    private TextView textViewWelcome,textViewQuote;
    private String name, email, address;
    private CustomerViewModel customerViewModel;
    private GoalViewModel goalViewModel;
    private WorkRequest uploadWorkRequest;
    private String cus_name;

    /**
     * Default constructor for the HomeFragment class
     */
    public HomeFragment() {
    }

    /**
     * Called when the fragment view is created.
     * Initializes necessary components, retrieves data, and sets up UI elements.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment UI should be attached to.
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The inflated View for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        // Initialize necessary components and views
        textViewWelcome = view.findViewById(R.id.welcome_message_textview);
        textViewQuote = view.findViewById(R.id.textviewQuote);
        goalViewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        uploadWorkRequest = new PeriodicWorkRequest.Builder(FirebaseWriteWorker.class, 1, TimeUnit.DAYS).build();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Get the customer name from login page using sharedPreferences
        cus_name = sharedPreferences.getString("customername", "");

        // Set the welcome message text with the customer's name
        textViewWelcome.setText(cus_name +", " +"embark on your goals!");

        // Create and configure the Retrofit instance for API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        // Make an API call to retrieve a quote and set it in the text view
        retrofitInterface.quote("eWXiX98Bqbn57cbKV5KN2A==Eo4oOf4lNxbJMQnT").enqueue(new Callback<ArrayList<TheySaidSoAPI>>() {
            @Override
            public void onResponse(Call<ArrayList<TheySaidSoAPI> >call, Response<ArrayList<TheySaidSoAPI>> response) {
                textViewQuote.setText(response.body().get(0).getQuote());
            }

            @Override
            public void onFailure(Call<ArrayList<TheySaidSoAPI>> call, Throwable t) {

                Log.i("RetrofitResponse", "onFail: " + t.getMessage());
            }
        });

        // Trigger the WorkManager job when the button is clicked
        Button triggerButton = view.findViewById(R.id.button_trigger);
        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);
                Toast.makeText(requireContext(), "WorkManager job triggered", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     Called when the view of the fragment is about to be destroyed
     **/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }

}










