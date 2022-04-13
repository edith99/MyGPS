package com.example.mygps.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygps.R;
import com.example.mygps.User;
import com.example.mygps.databinding.ActivityOpenPageBinding;
import com.example.mygps.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText email, password, name;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setListeners();

        mAuth = FirebaseAuth.getInstance();

        register = (Button) binding.register;
        register.setOnClickListener(this);

    }

    public void setListeners(){
        binding.haveAccount.setOnClickListener(x ->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = binding.email.getText().toString().trim();
        String name = binding.name.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if(name.isEmpty()){
            binding.name.setError("Full name is required!");
            binding.name.requestFocus();
            return;
        }
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
            binding.password.setError("Email is required!");
            binding.password.requestFocus();
            return;
        }
        if (password.length()<6){
            binding.password.setError("Min password length should be 6 characters!");
            binding.password.requestFocus();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"The user has been registered successfully!",Toast.LENGTH_LONG).show();
                                        binding.progressBar.setVisibility(View.GONE);
                                        //redirect to the Login page
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}