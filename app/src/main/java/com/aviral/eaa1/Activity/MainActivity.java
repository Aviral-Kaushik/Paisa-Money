package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.aviral.eaa1.Fragments.EarnMoneyFragment;
import com.aviral.eaa1.Fragments.ProfileFragment;
import com.aviral.eaa1.Fragments.ScratchCardFragment;
import com.aviral.eaa1.Fragments.SpinFragment;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private UserData userData;

    private Bundle userDataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDataBundle = new Bundle();

        onStart();

    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("Check", Context.MODE_PRIVATE);
        boolean isL = sharedPreferences.getBoolean("isLoggedIn", false);
        Log.d("AviralAPI", "onStart: isL " + isL);
        if (!isL) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            String email = sharedPreferences.getString("email", "");

            fetchUserData(email);
        }

//        if (isL) {
//            SharedPreferences userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
//            userData = new UserData(
//                    userPreferences.getString("name", ""),
//                    userPreferences.getString("email", ""),
//                    userPreferences.getString("uid", ""),
//                    userPreferences.getInt("disabled", 0),
//                    userPreferences.getInt("referred", 0),
//                    userPreferences.getString("date", ""),
//                    userPreferences.getString("time", ""),
//                    userPreferences.getString("referred_by", ""),
//                    userPreferences.getString("token", "-"),
//                    userPreferences.getString("referral_code", ""),
//                    userPreferences.getFloat("refer_earning", (float) 0.00f),
//                    userPreferences.getFloat("lifetime", (float) 0.00f),
//                    userPreferences.getString("is_rewarded", "")
//            );
//        }
    }



    private void fetchUserData(String email) {

        ApiBackendProvider backendProvider = new ApiBackendProvider(this);

        userData = backendProvider.fetchUserData(email);

        delay(2000, () -> {
            Bundle userDataBundle = new Bundle();
            userDataBundle.putParcelable(getString(R.string.user_data), userData);

            EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment();
            earnMoneyFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, earnMoneyFragment);
            fragmentTransaction.commit();
            setUpBottomNavigation();
        });

    }

    private void delay(int milliseconds, Runnable runnable) {
        Handler handler = new Handler();
        handler.postDelayed(runnable, milliseconds);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(binding.mainContainer,
                message,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void setUpBottomNavigation() {

        userDataBundle.putParcelable(getString(R.string.user_data), userData);

        LinearLayout btnEarnMoney = findViewById(R.id.btnEarnMoney);
        LinearLayout btnScratchCard = findViewById(R.id.btnScratchCard);
        LinearLayout btnSpin = findViewById(R.id.btnSpin);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);



        btnEarnMoney.setOnClickListener(view -> {
            EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment();
            earnMoneyFragment.setArguments(userDataBundle);
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
            scratchCardFragment.setArguments(userDataBundle);
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
            spinFragment.setArguments(userDataBundle);
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
            profileFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.replace(R.id.main_container, profileFragment);
            fragmentTransaction.commit();
        });


    }
}