package com.aviral.eaa1;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.aviral.eaa1.Fragments.EarnMoneyFragment;
import com.aviral.eaa1.Fragments.ProfileFragment;
import com.aviral.eaa1.Fragments.ScratchCardFragment;
import com.aviral.eaa1.Fragments.SpinFragment;
import com.aviral.eaa1.databinding.ActivityMainBinding;

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