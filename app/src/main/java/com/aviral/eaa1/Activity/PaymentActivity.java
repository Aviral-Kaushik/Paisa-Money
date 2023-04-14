package com.aviral.eaa1.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.Models.WithdrawRequest;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.RandomAlphaNumeric;
import com.aviral.eaa1.databinding.ActivityPaymentBinding;
import com.google.android.material.snackbar.Snackbar;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.payment_mode))
                && intent.hasExtra(getString(R.string.user_data))
                && intent.hasExtra(getString(R.string.withdraw_amount))) {
            setupViews(intent.getStringExtra(getString(R.string.payment_mode)),
                    intent.getParcelableExtra(getString(R.string.user_data)),
                    intent.getDoubleExtra(getString(R.string.withdraw_amount), 0.00));

        }
    }

    private void setupViews(String paymentMode, UserData userData, Double withdrawAmount) {

        if (paymentMode.equals(getString(R.string.paypal))) {

            binding.id.setHint("Paypal id");

        } else if (paymentMode.equals(getString(R.string.paytm))) {

            binding.id.setHint("Paytm UPI id");
            binding.imagePaymentMode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_paytm));
            binding.textPaymentMode.setText(getString(R.string.paytm));

        } else if (paymentMode.equals(getString(R.string.phonepe))) {

            binding.id.setHint("Phonepe UPI id");
            binding.imagePaymentMode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_phone_pay));
            binding.textPaymentMode.setText(getString(R.string.phonepe));

        } else if (paymentMode.equals(getString(R.string.gpay))) {

            binding.id.setHint("GPay UPI id");
            binding.imagePaymentMode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_google_pay));
            binding.textPaymentMode.setText(getString(R.string.gpay));

        }

        binding.btnContinue.setOnSlideCompleteListener(slideToActView -> {

            ApiBackendProvider backendProvider = new ApiBackendProvider(this);

            RandomAlphaNumeric alphaNumeric = new RandomAlphaNumeric();

            boolean isWithdrawRequestSuccessful = backendProvider.makeWithdrawRequest(
                    new WithdrawRequest(
                            userData.getUid(),
                            binding.name.getText().toString(),
                            binding.phoneNumber.getText().toString(),
                            binding.id.getText().toString(),
                            withdrawAmount,
                            "",
                            paymentMode,
                            alphaNumeric.generateAlphaNumeric(10)
                    )
            );

            Snackbar snackbar;
            if (isWithdrawRequestSuccessful) {
                snackbar = Snackbar.make(binding.layoutPayment,
                        "Your withdraw request has been successfully placed",
                        Snackbar.LENGTH_SHORT);
            } else {
                snackbar = Snackbar.make(binding.layoutPayment,
                        "Cannot place your withdraw request right now",
                        Snackbar.LENGTH_SHORT);
            }
            snackbar.show();

        });


    }
}