package com.aviral.eaa1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;

public class ScratchCardFragment extends Fragment {

    private FragmentScratchCardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        binding.scratchCard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.setVisibility(View.GONE);

                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(binding.amountWon.getText().toString());

                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}