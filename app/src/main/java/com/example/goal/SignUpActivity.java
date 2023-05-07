package com.example.goal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


//import com.example.goal.viewmodel.UserHelperClass;

import com.example.goal.entity.Customer;
import com.example.goal.viewmodel.CustomerViewModel;

import com.example.goal.viewmodel.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputLayout regName,regEmail,regAddress,regPassword;
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
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String name = regName.getEditText().getText().toString();
                String address = regAddress.getEditText().getText().toString();
                if (TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password)) {
                    String msg = "Empty Username or Password";
                } else if (password.length() < 6) {
                    String msg = "Password is too short";
                } else
//                    rootNode = FirebaseDatabase.getInstance();
//                    reference = rootNode.getReference("User");
//                    UserHelperClass helperClass = new UserHelperClass(name,email,password,address);
//                    reference.child(name).setValue(helperClass);
                    registerUser(email, password,name,address);
            }
        });
    }
    private void registerUser(String email_txt, String password_txt,String name, String address) {
        // To create username and password

        auth.createUserWithEmailAndPassword(email_txt,password_txt).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser firebaseuser = auth.getCurrentUser();
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("User");
                    Customer helperclass = new Customer(name,email_txt,address);
                    //UserHelperClass helperClass = new UserHelperClass(name,email_txt,password_txt,address);
                    reference.child(firebaseuser.getUid()).setValue(helperclass);
                    String msg = "Registration Successful";
                    startActivity(new Intent(SignUpActivity.this,
                            LoginActivity.class));
                }else {
                    String msg = "Registration Unsuccessful";
                    toastMsg(msg);
                }
            }
        });
    }
    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
