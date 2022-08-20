package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.apple.bank.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        register();
        login();
    }

    private void login() {
        binding.btnMainLogin.setOnClickListener(view -> {
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        });
    }

    private void register() {
        binding.btnMainSign.setOnClickListener(view -> {
            Intent intent=new Intent(this,SignUpActivity.class);
            startActivity(intent);
        });
    }
}