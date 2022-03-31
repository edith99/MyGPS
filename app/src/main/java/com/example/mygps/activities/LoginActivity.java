package com.example.mygps.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mygps.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}