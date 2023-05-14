package com.example.goal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.example.goal.viewmodel.UserHelperClass;

import com.example.goal.entity.Customer;
import com.example.goal.viewmodel.CustomerViewModel;

import com.example.goal.viewmodel.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Represents the sign-up screen of the application.
 * Allows users to create a new account.
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "HomeFragment";
    private FirebaseAuth auth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private TextInputLayout regName,regEmail,regAddress,regPassword;
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d).{7,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        auth = FirebaseAuth.getInstance();

        Button registerButton=findViewById(R.id.signUpButton);

        Button loginButton = findViewById(R.id.LogInButton);

        regName = findViewById(R.id.signUp_name);
        regEmail = findViewById(R.id.signUp_email);
        regPassword = findViewById(R.id.signUp_password);
        regAddress = findViewById(R.id.signUp_address);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String name = regName.getEditText().getText().toString();
                String address = regAddress.getEditText().getText().toString();
                Matcher match = pattern.matcher(password);
                boolean matchFound = match.find();
                //matchFound = true;
                if (TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password)) {
                    String msg = "Empty Username or Password";
                } else if (!matchFound) {
                    regPassword.setError("password must be more than 7 characters and must have a alphabet");
                    regPassword.requestFocus();
                    //String msg = "Password is too short";
                } else if(TextUtils.isEmpty(address)) {
                    regAddress.setError("enter your address");
                    regAddress.requestFocus();
                }else
                    registerUser(email, password,name,address);
            }
        });

    }
    /**
     * Registers a new user account using the provided email and password.
     *
     * @param email_txt    The email address of the user.
     * @param password_txt The password for the user account.
     * @param name     The name of the user.
     * @param address  The address of the user.
     */
    private void registerUser(String email_txt, String password_txt,String name, String address) {
        // To create username and password

        auth.createUserWithEmailAndPassword(email_txt,password_txt).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // Registration successful
                    FirebaseUser firebaseuser = auth.getCurrentUser();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("User");
                    Log.d(TAG, "writeDataToFirebase() - Customer data written to Firebase at " + new Date().toString());
                    // Generate a unique user ID
                    int userId = generateUserId();
                    // Create a Customer object with the user data
                    Customer helperclass = new Customer(userId, name,email_txt,address);
                    // Insert the Customer object into the local Room database using the ViewModel
                    CustomerViewModel customerViewModel = new ViewModelProvider(SignUpActivity.this).get(CustomerViewModel.class);
                    customerViewModel.insert(helperclass);
                    // Insert the Customer object into the Firebase Database
                    reference.child(String.valueOf(userId)).setValue(helperclass);
                    String msg = "Registration Successful";
                    startActivity(new Intent(SignUpActivity.this,
                            LoginActivity.class));
                }else {
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        regEmail.setError("email and password do not match");
                        regEmail.requestFocus();
                        //regPassword.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        regEmail.setError("There is user with this email,try another one!");
                    }catch (Exception e){
                        String msg = "registration unsuccessful!!";
                        toastMsg(msg);
                    }
                }
            }
        });
    }
    /**
     * Generates a unique user ID for registration.
     *
     * @return The generated user ID.
     */
    private int generateUserId() {
        // Get a reference to the "users" node in the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Generate a random ID and check if it already exists in the database
        int userId;
        do {
            // Generate a random ID between 10000 and 99999 (inclusive)
            userId = new Random().nextInt(90000) + 10000;
        } while (isUserIdExists(usersRef, userId));

        return userId;
    }
    /**
     * Checks if a user ID already exists in the Firebase Realtime Database.
     *
     * @param usersRef The reference to the "users" node in the Firebase Realtime Database.
     * @param userId   The user ID to check for existence.
     * @return True if the user ID exists, false otherwise.
     */
    private boolean isUserIdExists(DatabaseReference usersRef, int userId) {
        final boolean[] exists = {false};
        usersRef.child(String.valueOf(userId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exists[0] = snapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return exists[0];
    }
    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
