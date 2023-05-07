package com.example.goal.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.goal.R;
import com.example.goal.databinding.HomeFragmentBinding;
import com.example.goal.entity.Customer;
import com.example.goal.viewmodel.CustomerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding addBinding;
    private FirebaseAuth authProfile;
    private TextView textViewWelcome;
    private String name,email,address;
    private CustomerViewModel customerViewModel;
    public HomeFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        authProfile = FirebaseAuth.getInstance();
        textViewWelcome = view.findViewById(R.id.welcome_message_textview);
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "not Found the user information", Toast.LENGTH_SHORT).show();
        }else {
            showUserProfile(firebaseUser);
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String useId = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("User");
        referenceProfile.child(useId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel.class);
                Customer customer = snapshot.getValue(Customer.class);
                if(customer != null){
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

}
