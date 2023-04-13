package com.aviral.eaa1.Fragments;

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
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;

import java.util.Random;

public class ScratchCardFragment extends Fragment {

    private FragmentScratchCardBinding binding;

    private UserData userData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        View view = binding.getRoot();

        binding.amountWon.setText("₹" + generateRandomNumber());

        binding.btnBalance.setText(userData.getBalance());

        binding.scratchCard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.setVisibility(View.GONE);

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

        return view;
    }

    private double generateRandomNumber() {
        double min = 0.10;
        double max = 0.50;

        Random random = new Random();

        double randomDouble =  min + (max - min) * random.nextDouble();

        return Math.round(randomDouble * 100.0) / 100.0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}