package com.aviral.eaa1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.databinding.FragmentSpinBinding;

import java.util.Random;

public class SpinFragment extends Fragment {

    private FragmentSpinBinding binding;

    final double[] sectors = {0.20, 0.75, 0.05, 0.25, 0.15, 1.00, 0.10, 0.50};
    final int[] sectorsDegree = new int[sectors.length];

    int randomSectorIndex = 0;
    boolean spinning = false;

    Random random = new Random();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSpinBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        binding.btnSpin.setOnSlideCompleteListener(slideToActView -> startSpin());

        return view;
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

                // Save Earned Amount in Dialog Fragment
                spinning = false;

                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog("₹" + String.valueOf(earnedAmount));

                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.spinningWheel.startAnimation(rotateAnimation);

    }

    private int generateRandomDegreeToSpin() {
        return (360 * sectors.length) + sectorsDegree[randomSectorIndex] + 10;
    }

    private void generateSectorDegree() {
        int sectorDegree = 360 / sectors.length;

        for (int i = 0; i < sectors.length; i++) {
            sectorsDegree[i] = (i + 1) * sectorDegree;
        }
    }
}