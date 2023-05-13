package com.example.goal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.goal.database.CustomerDatabase;
import com.example.goal.entity.Customer;
import com.example.goal.repository.CustomerRepository;
import com.example.goal.viewmodel.CustomerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import androidx.work.WorkRequest;

public class LoginActivity extends AppCompatActivity {
    private WorkRequest uploadWorkRequest;
    private FirebaseAuth auth;
    private CustomerDatabase db; // Add a field for the database
    private CustomerViewModel customerViewModel;
    private SharedPreferences sharedPreferences;

    private  TextInputLayout emailEditText,passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        db = CustomerDatabase.getInstance(this); // Initialize the database instance
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
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
                String txt_Email = emailEditText.getEditText().getText().toString();
                String txt_Pwd = passwordEditText.getEditText().getText().toString();
                if(TextUtils.isEmpty(txt_Email)){
                    emailEditText.setError("email is required");
                    emailEditText.requestFocus();
                }
                if(TextUtils.isEmpty(txt_Pwd)){
                    passwordEditText.setError("password is empty");
                    passwordEditText.requestFocus();
                }else loginUser(txt_Email,txt_Pwd);
            }
        });
    }
    public SharedPreferences getMySharedPreferences() {
        return getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    private void loginUser(String txt_email, String txt_pwd) {
        auth.signInWithEmailAndPassword(txt_email,txt_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ((task.isSuccessful())){
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
                    int id = 0;
                    Customer customer = new Customer(id, name, email, address);
                    //customerViewModel.insert(customer);
                    // Insert the Customer object into the Room database using AsyncTask
                    //new InsertCustomerTask().execute(customer);

                    customerViewModel.getCustomerlByEmail(email).observe(LoginActivity.this, new Observer<Customer>() {
                        @Override
                        public void onChanged(Customer customer) {
                            if (customer != null) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("customerId", customer.getUid());
                                editor.putString("customername", customer.getName());
                                editor.putString("customeraddress", customer.getAddress());
                                editor.apply();
                                Log.d("LoginActivity", "Customertest: " + customer.toString());

                            }
                        }
                    });




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


                        }
                    });
                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        emailEditText.setError("user does not exist");
                        emailEditText.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        passwordEditText.setError("password does not match");
                        passwordEditText.requestFocus();
                    }catch (Exception e){
                        String msg = "Login unsuccessful!!";
                        toastMsg(msg);
                    }
                }
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