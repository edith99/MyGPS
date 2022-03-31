package com.example.mygps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mygps.activities.LoginActivity;
import com.example.mygps.activities.OpenPageActivity;
import com.example.mygps.activities.RegisterActivity;
import com.example.mygps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, OpenPageActivity.class));
                finish();
            }
        }, 2000);

    }




}