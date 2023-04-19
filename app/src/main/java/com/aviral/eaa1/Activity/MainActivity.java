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
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private UserData userData;

    private Bundle userDataBundle;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        userDataBundle = new Bundle();

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
    }

    private void fetchUserData(String email) {

        ApiBackendProvider backendProvider = new ApiBackendProvider(this);

        userData = backendProvider.fetchUserData(email);

        delay(() -> {

            loadingDialog.dismiss();

            Bundle userDataBundle = new Bundle();
            userDataBundle.putParcelable(getString(R.string.user_data), userData);

            EarnMoneyFragment earnMoneyFragment = new EarnMoneyFragment();
            earnMoneyFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, earnMoneyFragment);
            fragmentTransaction.commitAllowingStateLoss();
            setUpBottomNavigation();
        });

    }

    private void delay(Runnable runnable) {
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);
    }

    public void setUpBottomNavigation() {

        userDataBundle.putParcelable(getString(R.string.user_data), userData);

        LinearLayout btnEarnMoney = findViewById(R.id.btnEarnMoney);
        LinearLayout btnScratchCard = findViewById(R.id.btnBottomScratchCard);
        LinearLayout btnSpin = findViewById(R.id.btnBottomSpin);
        LinearLayout btnProfile = findViewById(R.id.btnBottomProfile);

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
            fragmentTransaction.attach(earnMoneyFragment);
            fragmentTransaction.detach(earnMoneyFragment);
            fragmentTransaction.attach(earnMoneyFragment);
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
            fragmentTransaction.attach(scratchCardFragment);
            fragmentTransaction.detach(scratchCardFragment);
            fragmentTransaction.attach(scratchCardFragment);
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
            fragmentTransaction.attach(spinFragment);
            fragmentTransaction.detach(spinFragment);
            fragmentTransaction.attach(spinFragment);
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
            fragmentTransaction.attach(profileFragment);
            fragmentTransaction.detach(profileFragment);
            fragmentTransaction.attach(profileFragment);
            fragmentTransaction.replace(R.id.main_container, profileFragment);
            fragmentTransaction.commit();
        });


    }
}