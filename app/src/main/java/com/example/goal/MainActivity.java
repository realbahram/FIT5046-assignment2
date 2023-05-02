package com.example.goal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton =findViewById(R.id.SignUpButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  startActivity(new Intent(MainActivity.this,
                                                          SignUp.class));
                                              }
                                          }
        );
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
        // call the object and provide it with email id and password
        auth.signInWithEmailAndPassword(txt_email,
                txt_pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String msg = "Login Successful";
                toastMsg(msg);
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
            }
        });
    }
    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}