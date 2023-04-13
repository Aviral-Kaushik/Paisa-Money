package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aviral.eaa1.Fragments.EarnMoneyFragment;
import com.aviral.eaa1.Fragments.ProfileFragment;
import com.aviral.eaa1.Fragments.ScratchCardFragment;
import com.aviral.eaa1.Fragments.SpinFragment;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiConstants;
import com.aviral.eaa1.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, earnMoneyFragment);
        fragmentTransaction.commit();
        setUpBottomNavigation();


    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
        boolean isL = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isL){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void setUpBottomNavigation() {
        LinearLayout btnEarnMoney = findViewById(R.id.btnEarnMoney);
        LinearLayout btnScratchCard = findViewById(R.id.btnScratchCard);
        LinearLayout btnSpin = findViewById(R.id.btnSpin);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        btnEarnMoney.setOnClickListener(view -> {
            EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.replace(R.id.main_container, earnMoneyFragment);
            fragmentTransaction.commit();
        });

        btnScratchCard.setOnClickListener(view -> {
            ScratchCardFragment scratchCardFragment = new ScratchCardFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.replace(R.id.main_container, scratchCardFragment);
            fragmentTransaction.commit();
        });

        btnSpin.setOnClickListener(view -> {
            SpinFragment spinFragment = new SpinFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.replace(R.id.main_container, spinFragment);
            fragmentTransaction.commit();
        });

        btnProfile.setOnClickListener(view -> {
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    );
            fragmentTransaction.replace(R.id.main_container, profileFragment);
            fragmentTransaction.commit();
        });
    }
}