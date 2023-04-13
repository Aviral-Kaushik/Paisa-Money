package com.aviral.eaa1.Dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class WonPriceClaimDialog extends DialogFragment {

    private final Context context;
    private final String wonAmount;
    private final UserData userData;

    private View view;

    public WonPriceClaimDialog(Context context, String wonAmount, UserData userData) {
        this.context = context;
        this.wonAmount = wonAmount;
        this.userData = userData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_won_price_dialog, container, false);

        ConstraintLayout wonPriceClaimed = view.findViewById(R.id.wonPrice);

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Objects.requireNonNull(getDialog()).getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;

        wonPriceClaimed.setOnClickListener(view1 -> {

            updateUserBalance();

            dismiss();
        });

        TextView amount = view.findViewById(R.id.tv_amount);
        amount.setText(wonAmount);

        return view;
    }

    private void updateUserBalance() {

        ApiBackendProvider backendProvider = new ApiBackendProvider(context);

        boolean isBalanceUpdatedSuccessfully = backendProvider.updateUserBalance(
                userData.getUid(),
                wonAmount.substring(1)
        );

        Snackbar snackbar;
        if (isBalanceUpdatedSuccessfully) {
            snackbar = Snackbar.make(view,
                    "Your balance Updated Successfully",
                    Snackbar.LENGTH_SHORT);

        } else {
            snackbar = Snackbar.make(view,
                    "Cannot update your balance at this moment",
                    Snackbar.LENGTH_SHORT);
        }
        snackbar.show();


        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);

    }


}
