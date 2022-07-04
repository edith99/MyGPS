package com.example.mygps.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.mygps.databinding.ActivityOpenPageBinding;

public class OpenPageActivity extends AppCompatActivity {

    private ActivityOpenPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    public void setListeners(){
        binding.register.setOnClickListener(x ->
        {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        });

        binding.login.setOnClickListener(x ->
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        });
    }
}
