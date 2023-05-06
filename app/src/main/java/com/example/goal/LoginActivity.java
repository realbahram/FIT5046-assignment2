package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.goal.database.CustomerDatabase;
import com.example.goal.entity.Customer;
import com.example.goal.fragment.HomeFragment;
import com.example.goal.repository.CustomerRepository;
import com.example.goal.viewmodel.CustomerViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private CustomerDatabase db; // Add a field for the database
    private CustomerViewModel customerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db = CustomerDatabase.getInstance(this); // Initialize the database instance
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton =findViewById(R.id.SignUpButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,
                        SignUpActivity.class));
            }
        });
        Button loginButton =findViewById(R.id.LogIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_Email = emailEditText.getText().toString();
                String txt_Pwd = passwordEditText.getText().toString();
                loginUser(txt_Email,txt_Pwd);
            }
        });
    }

    private void loginUser(String txt_email, String txt_pwd) {
        auth.signInWithEmailAndPassword(txt_email, txt_pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String msg = "Login Successful";
                toastMsg(msg);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                String name;
                if (user != null) {
                    name = user.getDisplayName();
                    if (name == null) {
                        name = "User";
                    }
                } else {
                    name = "User";
                }

                String email = user.getEmail();
                if (email == null) {
                    email = "Unknown";
                }
                //Log.d("LoginActivity", "Customer Name: " +  name);
                String address = "dummy";
                Customer customer = new Customer(name, email, address);
                customerViewModel.insert(customer);
                // Insert the Customer object into the Room database using AsyncTask
                new InsertCustomerTask().execute(customer);

                // Pass the name to the HomeActivity on the UI thread using a Handler
                Handler handler = new Handler(Looper.getMainLooper());
                String finalName = name;
                String finalEmail = email;
                Log.d("LoginActivity", "Customer Name: " +  finalName);
                Log.d("LoginActivity", "Customer Email: " +  finalEmail);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("name", finalName);
                        intent.putExtra("email", finalEmail);
                        startActivity(intent);

                        //CustomerRepository customerRepository = new CustomerRepository(getApplication());
                        //List<Customer> allCustomers = customerRepository.getAllCustomersList();

                        //for (Customer customer : allCustomers) {
                    }
            });
            }
        });
    }

    // Define an AsyncTask to insert a Customer object into the Room database
    private class InsertCustomerTask extends AsyncTask<Customer, Void, Void> {

        @Override
        protected Void doInBackground(Customer... customers) {
            CustomerRepository repository = new CustomerRepository(getApplication());
            repository.insert(customers[0]);
            return null;
        }
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}