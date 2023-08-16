package com.example.technicalsupport.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.technicalsupport.databinding.ActivitySplachBinding;

public class SplashActivity extends AppCompatActivity {
ActivitySplachBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding=ActivitySplachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },2000);
    }
    }
