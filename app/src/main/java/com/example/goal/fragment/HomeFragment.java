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

public class HomeFragment extends Fragment {
    private HomeFragmentBinding addBinding;
    private FirebaseAuth authProfile;
    private TextView textViewWelcome,textViewQuote;
    private String name, email, address;
    private CustomerViewModel customerViewModel;
    private PieChart pieChart;
    private GoalViewModel goalViewModel;

    private WorkRequest uploadWorkRequest;
    private String cus_name;

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
        textViewQuote = view.findViewById(R.id.textviewQuote);
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        goalViewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        uploadWorkRequest = new PeriodicWorkRequest.Builder(FirebaseWriteWorker.class, 1, TimeUnit.DAYS).build();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        cus_name = sharedPreferences.getString("customername", "");
        //Log.d("customernametest", "customernametest: " + cus_name);
        textViewWelcome.setText(cus_name +", " +"embark on your goals!");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.api-ninjas.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
////        retrofitInterface.quote("eWXiX98Bqbn57cbKV5KN2A==Eo4oOf4lNxbJMQnT").enqueue(new Callback<List<TheySaidSoAPI>>() {
////            @Override
////            public void onResponse(Call<List<TheySaidSoAPI>> call, Response<List<TheySaidSoAPI>> response) {
////                List<TheySaidSoAPI> list = response.body();
////                String result = list.get(0).quote;
////                textViewQuote.setText(result);
////            }
////
////            @Override
////            public void onFailure(Call<List<TheySaidSoAPI>> call, Throwable t) {
////                textViewQuote.setText(t.getMessage());
////            }
////        });
////        retrofitInterface.quote("eWXiX98Bqbn57cbKV5KN2A==Eo4oOf4lNxbJMQnT").enqueue(new Call<List<TheySaidSoAPI>>())
        retrofitInterface.quote("eWXiX98Bqbn57cbKV5KN2A==Eo4oOf4lNxbJMQnT").enqueue(new Callback<ArrayList<TheySaidSoAPI>>() {
            @Override
            public void onResponse(Call<ArrayList<TheySaidSoAPI> >call, Response<ArrayList<TheySaidSoAPI>> response) {
               // Log.i("RetrofitResponse", "onResponse: " + response.body().get(0).getQuote());
                //textViewQuote.setText("sdasdasdasdasd");
                textViewQuote.setText(response.body().get(0).getQuote());
            }

            @Override
            public void onFailure(Call<ArrayList<TheySaidSoAPI>> call, Throwable t) {

                Log.i("RetrofitResponse", "onFail: " + t.getMessage());

               // textViewQuote.setText(t.getMessage());
            }
        });

        Button triggerButton = view.findViewById(R.id.button_trigger);
        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest);
                Toast.makeText(requireContext(), "WorkManager job triggered", Toast.LENGTH_SHORT).show();
            }
        });


        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "not Found the user information", Toast.LENGTH_SHORT).show();
        } else {
            showUserProfile(firebaseUser);
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

                    Log.d("customernametest", "customernametest: " + cus_name);

                    textViewWelcome.setText("Welcome " + cus_name + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "something Gone wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}








