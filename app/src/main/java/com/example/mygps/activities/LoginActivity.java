package com.example.mygps.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygps.R;
import com.example.mygps.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button logIn;
    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListeners();

        logIn = (Button) binding.login;
        logIn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
    public void setListeners(){
        binding.newAccount.setOnClickListener(x ->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        binding.forgotPassword.setOnClickListener(y ->{
            Intent intent2 = new Intent(this,ForgotPasswordActivity.class);
            startActivity(intent2);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                logInUser();
                break;
        }
    }
    private void logInUser() {
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if(email.isEmpty()){
            binding.email.setError("Email is required!");
            binding.email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Please provide a valid email.");
            binding.email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.password.setError("Password is required!");
            binding.password.requestFocus();
            return;
        }
        if (password.length()<6){
            binding.password.setError("Min password length should be 6 characters!");
            binding.password.requestFocus();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                    //redirect to user profile
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Failed to log in! Please check your credentials!", Toast.LENGTH_LONG).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}