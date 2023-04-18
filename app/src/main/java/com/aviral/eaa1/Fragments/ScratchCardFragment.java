package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;

import java.util.Random;

public class ScratchCardFragment extends Fragment {

    private FragmentScratchCardBinding binding;

    private UserData userData;

    private int chancesLeft;

    private LoadingDialog loadingDialog;

    private ApiBackendProvider backendProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        backendProvider = new ApiBackendProvider(requireContext());

        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();

        binding.amountWon.setText(String.format("₹%s", generateRandomNumber()));

        binding.btnBalance.setText(userData.getBalance());

        getChances();

        binding.scratchCard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.setVisibility(View.GONE);

                decrementChances();

                updateUserBalance(binding.amountWon.getText().toString().substring(1));

                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(binding.amountWon.getText().toString());

                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });

        return view;
    }

    private void updateUserBalance(String earnedAmount) {

        backendProvider.updateUserBalance(
                userData.getUid(),
                String.valueOf(earnedAmount)
        );

        fetchUserData(userData.getEmail());
    }

    private void fetchUserData(String email) {
        userData = backendProvider.fetchUserData(email);

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();

            Bundle userDataBundle = new Bundle();
            userDataBundle.putParcelable(getString(R.string.user_data), userData);

            ScratchCardFragment scratchCardFragment = new ScratchCardFragment();
            scratchCardFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction = getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.replace(R.id.main_container, scratchCardFragment);
            fragmentTransaction.commit();
        }, 2000);

    }

    private void getChances() {
        SharedPreferences chancesPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        chancesLeft = chancesPreferences.getInt("chancesLeft", 10);

        if (chancesLeft > 1) {
            binding.tvChances.setText(chancesLeft + " Chances Left");
        } else if (chancesLeft == 1) {
            binding.tvChances.setText(chancesLeft + " Chance Left");
        } else {
            binding.tvChances.setText(chancesLeft + " Chance Left");

            binding.scratchCard.setEraserMode();

            binding.scratchCard.setVisibility(View.GONE);
            binding.scratchImage.setVisibility(View.VISIBLE);


            Toast.makeText(requireContext(), "0 Chances Left", Toast.LENGTH_SHORT).show();
        }

        loadingDialog.dismiss();

    }

    private void decrementChances() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (chancesLeft > 0) {
            editor.putInt("chancesLeft", (chancesLeft - 1));
            editor.apply();
        }

    }

    private double generateRandomNumber() {
        double min = 0.10;
        double max = 0.50;

        Random random = new Random();

        double randomDouble = min + (max - min) * random.nextDouble();

        return Math.round(randomDouble * 100.0) / 100.0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}