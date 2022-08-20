package com.apple.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.apple.bank.databinding.ActivityOperationsBinding;

public class OperationsActivity extends AppCompatActivity {
    ActivityOperationsBinding binding;
    FragmentTransaction transaction;
    Fragment fragmentTransf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOperationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewQr();
        history();
        with();
    }

    private void viewQr() {
        binding.btnTransfer.setOnClickListener(view -> {
            Intent intent=new Intent(this,TransferActivity.class);
            startActivity(intent);
        });
    }
    private void history(){
        binding.btnHistoy.setOnClickListener(view -> {
            Intent intent=new Intent(this,HistoyActivity.class);
            startActivity(intent);
        });
    }
    private void with(){
        binding.btnWith.setOnClickListener(view -> {
            Intent intent=new Intent(this,WithdrawalActivity.class);
            startActivity(intent);
        });
    }
}