package com.aviral.eaa1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.aviral.eaa1.R;
import com.aviral.eaa1.databinding.ActivityScratchCardBinding;

public class ScratchCardActivity extends AppCompatActivity {

    private ActivityScratchCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        LinearLayout btnEarnMoney = findViewById(R.id.btnEarnMoney);
        LinearLayout btnSpin = findViewById(R.id.btnSpin);
        LinearLayout btnOthers = findViewById(R.id.btnOthers);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);

        btnSpin.setOnClickListener(view -> startActivity(
                new Intent(this, SpinActivity.class)));

        btnEarnMoney.setOnClickListener(view -> startActivity(
                new Intent(this, MainActivity.class)));
    }
}