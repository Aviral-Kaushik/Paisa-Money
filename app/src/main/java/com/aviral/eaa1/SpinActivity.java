package com.aviral.eaa1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aviral.eaa1.databinding.ActivitySpinBinding;
import com.ncorti.slidetoact.SlideToActView;

import java.util.Random;

public class SpinActivity extends AppCompatActivity {

    private ActivitySpinBinding binding;

    final double[] sectors = {0.20, 0.75, 0.05, 0.25, 0.15, 1.00, 0.10, 0.50};
    final int[] sectorsDegree = new int[sectors.length];

    int randomSectorIndex = 0;
    boolean spinning = false;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSpin.setOnSlideCompleteListener(slideToActView -> startSpin());

        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        LinearLayout btnScratchCard = findViewById(R.id.btnScratchCard);
        LinearLayout btnEarnMoney = findViewById(R.id.btnEarnMoney);
        LinearLayout btnOthers = findViewById(R.id.btnOthers);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);

        btnEarnMoney.setOnClickListener(view -> startActivity(
                new Intent(this, MainActivity.class)));

        btnScratchCard.setOnClickListener(view -> startActivity(
                new Intent(this, ScratchCardActivity.class)));
    }

    private void startSpin() {
        generateSectorDegree();

        if (!spinning) {
            spin();
            spinning = true;
        }
    }

    private void spin() {
        randomSectorIndex = random.nextInt(sectors.length);

        int randomDegree = generateRandomDegreeToSpin();

        RotateAnimation rotateAnimation = new RotateAnimation(0, randomDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);

        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                double earnedAmount = sectors[sectors.length - (randomSectorIndex + 1)];

                // Save Earned Amount Here

                Toast.makeText(SpinActivity.this,
                        "You Earned " + earnedAmount, Toast.LENGTH_SHORT).show();

                spinning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.spinningWheel.startAnimation(rotateAnimation);

    }

    private int generateRandomDegreeToSpin() {
        return (360 * sectors.length) + sectorsDegree[randomSectorIndex];
    }

    private void generateSectorDegree() {
        int sectorDegree = 360 / sectors.length;

        for (int i = 0; i < sectors.length; i++) {
            sectorsDegree[i] = (i + 1) * sectorDegree;
        }
    }
}