package com.aviral.eaa1.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.aviral.eaa1.R;

import java.util.Objects;

public class WonPriceClaimDialog extends DialogFragment {

    private final String wonAmount;

    public WonPriceClaimDialog(String wonAmount) {
        this.wonAmount = wonAmount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_won_price_dialog, container, false);

        ConstraintLayout wonPriceClaimed = view.findViewById(R.id.wonPrice);

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Objects.requireNonNull(getDialog()).getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;

        wonPriceClaimed.setOnClickListener(view1 -> dismiss());

        TextView amount = view.findViewById(R.id.tv_amount);
        amount.setText(roundOfNumber(wonAmount));

        return view;
    }

    private String roundOfNumber(String number) {

        if (number.length() == 4) {
            number += "0";
        }

        return number;
    }



}
