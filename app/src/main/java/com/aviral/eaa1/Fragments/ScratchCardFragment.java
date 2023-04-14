package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class ScratchCardFragment extends Fragment {

    private FragmentScratchCardBinding binding;

    private UserData userData;

    private int chancesLeft;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();

        binding.amountWon.setText("₹" + generateRandomNumber());

        binding.btnBalance.setText(userData.getBalance());

        getChances();

        if (chancesLeft > 0) {
            binding.scratchCard.setRevealListener(new ScratchView.IRevealListener() {
                @Override
                public void onRevealed(ScratchView scratchView) {
                    scratchView.setVisibility(View.GONE);

                    decrementChances();

                    WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
                            requireContext(),
                            binding.amountWon.getText().toString(),
                            userData);

                    wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");

                }

                @Override
                public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(binding.layoutScratch,
                    "You Cannot Scratch Card as 0 Chances Left",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        return view;
    }

    private void getChances() {
        SharedPreferences chancesPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        chancesLeft = chancesPreferences.getInt("chancesLeft", 10);

        if (chancesLeft > 1) {
            binding.tvChances.setText(chancesLeft + " Chances Left");
        } else {
            binding.tvChances.setText(chancesLeft + " Chance Left");
        }

        loadingDialog.dismiss();

    }

    private void decrementChances() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (chancesLeft > 0) {
            editor.putInt("chancesLeft", (chancesLeft-1));
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