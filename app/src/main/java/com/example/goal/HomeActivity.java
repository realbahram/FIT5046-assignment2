package com.example.goal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.goal.databinding.HomeActivityBinding;

public class HomeActivity extends AppCompatActivity {
    private HomeActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.appBar.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_add_fragment)
//to display the Navigation button as a drawer symbol,not being shown as an Up
        //button
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager= getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar,navController,
                mAppBarConfiguration);


    }
}

