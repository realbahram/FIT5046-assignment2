package com.example.goal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.goal.databinding.HomeActivityBinding;
import com.example.goal.entity.Customer;
import com.example.goal.viewmodel.CustomerViewModel;

public class HomeActivity extends AppCompatActivity {
    private HomeActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private CustomerViewModel customerViewModel;
    /**
     * Called when the activity is created.
     * Sets up the layout, navigation, and user information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        setSupportActionBar(binding.appBar.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                mAppBarConfiguration);

        String email = getIntent().getStringExtra("email");
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    /**
     * Handles the logout process.
     * Starts the LoginActivity and finishes the current activity.
     */
    private void logout() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * AsyncTask to retrieve the customer based on the email.
     */
    private class GetCustomerAsyncTask extends AsyncTask<String, Void, Customer> {
        @Override
        protected Customer doInBackground(String... params) {
            String email = params[0];
            return customerViewModel.getCustomerByEmail(email);
        }

        @Override
        protected void onPostExecute(Customer customer) {
            // Set the customer in the view model
            super.onPostExecute(customer);
            String username = (customer != null && !TextUtils.isEmpty(customer.getName())) ?
                    customer.getName() : "User";

            // Display the welcome message with the name in a TextView
            if (customer != null) {
                // Display the welcome message with the name in a TextView
                TextView welcomeMessageTextView = findViewById(R.id.welcome_message_textview);
                welcomeMessageTextView.setText(username);
            }
        }

    }
}




