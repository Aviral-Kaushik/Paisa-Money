package com.aviral.eaa1.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.databinding.ActivityWithdrawBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public class WithdrawActivity extends AppCompatActivity {

    private ActivityWithdrawBinding binding;

    // Selection Color #FFBC36

    private boolean isPayPalSelected = false,
            isPaytmSelected = false,
            isPhonePeSelected = false,
            isGooglePaySelected = false;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.user_data))) {
            userData = intent.getParcelableExtra(getString(R.string.user_data));
        }

        binding.amount.setText(String.format("₹%s ≈ $%s",
                userData.getBalance(),
                INRTODollar(Double.parseDouble(userData.getBalance()))));

        binding.userName.setText(userData.getName());
        binding.tvUserName.setText(userData.getName());

        binding.payapalAmount.setText(String.format("$%s", INRTODollar(Double.parseDouble(userData.getBalance()))));
        binding.paytmAmount.setText(String.format("₹%s", userData.getBalance()));
        binding.phonePeAmount.setText(String.format("₹%s", userData.getBalance()));
        binding.googlePayAmount.setText(String.format("₹%s", userData.getBalance()));

        binding.paypal.setOnClickListener(view -> {
            resetAllPaymentMethods();

            isPayPalSelected = true;

            binding.paypal.setBackgroundColor(getResources().getColor(R.color.selected_payment_mode));
            binding.tvPaypal.setTextColor(getResources().getColor(R.color.white));
            binding.payapalAmount.setTextColor(getResources().getColor(R.color.white));

        });

        binding.paytm.setOnClickListener(view -> {
            resetAllPaymentMethods();

            isPaytmSelected = true;

            binding.paytm.setBackgroundColor(getResources().getColor(R.color.selected_payment_mode));
            binding.tvPaytm.setTextColor(getResources().getColor(R.color.white));
            binding.paytmAmount.setTextColor(getResources().getColor(R.color.white));

        });

        binding.phonePe.setOnClickListener(view -> {
            resetAllPaymentMethods();

            isPhonePeSelected = true;

            binding.phonePe.setBackgroundColor(getResources().getColor(R.color.selected_payment_mode));
            binding.tvPhonePe.setTextColor(getResources().getColor(R.color.white));
            binding.phonePeAmount.setTextColor(getResources().getColor(R.color.white));

        });

        binding.googlePay.setOnClickListener(view -> {
            resetAllPaymentMethods();

            isGooglePaySelected = true;

            binding.googlePay.setBackgroundColor(getResources().getColor(R.color.selected_payment_mode));
            binding.tvGooglePay.setTextColor(getResources().getColor(R.color.white));
            binding.googlePayAmount.setTextColor(getResources().getColor(R.color.white));

        });

        binding.btnContinue.setOnSlideCompleteListener(view -> navigateToPaymentScreen());
    }

    private void resetAllPaymentMethods() {

        binding.paypal.setBackground(AppCompatResources.getDrawable(this,
                R.drawable.payment_method_unselected_background));
        binding.tvPaypal.setTextColor(getResources().getColor(R.color.payment_method_tv));
        binding.payapalAmount.setTextColor(getResources().getColor(R.color.black));

        binding.paytm.setBackground(AppCompatResources.getDrawable(this,
                R.drawable.payment_method_unselected_background));
        binding.tvPaytm.setTextColor(getResources().getColor(R.color.payment_method_tv));
        binding.paytmAmount.setTextColor(getResources().getColor(R.color.black));

        binding.phonePe.setBackground(AppCompatResources.getDrawable(this,
                R.drawable.payment_method_unselected_background));
        binding.tvPhonePe.setTextColor(getResources().getColor(R.color.payment_method_tv));
        binding.phonePeAmount.setTextColor(getResources().getColor(R.color.black));

        binding.googlePay.setBackground(AppCompatResources.getDrawable(this,
                R.drawable.payment_method_unselected_background));
        binding.tvGooglePay.setTextColor(getResources().getColor(R.color.payment_method_tv));
        binding.googlePayAmount.setTextColor(getResources().getColor(R.color.black));

        isPayPalSelected = false;
        isPaytmSelected = false;
        isGooglePaySelected = false;
        isPhonePeSelected = false;
    }

    private void navigateToPaymentScreen() {

        if (isPayPalSelected) {

            double withdrawAmount = Double.parseDouble(binding.payapalAmount.getText().toString().substring(1));

            if (withdrawAmount > 1.4) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.paypal));
                intent.putExtra(getString(R.string.user_data), userData);
                intent.putExtra(getString(R.string.withdraw_amount), withdrawAmount);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be $1.4");

            }

        } else if (isPaytmSelected) {

            double withdrawAmount = Double.parseDouble(binding.paytmAmount.getText().toString().substring(1));

            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.paytm));
                intent.putExtra(getString(R.string.user_data), userData);
                intent.putExtra(getString(R.string.withdraw_amount), withdrawAmount);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be ₹100");

            }

        } else if (isPhonePeSelected) {

            double withdrawAmount = Double.parseDouble(binding.phonePeAmount.getText().toString().substring(1));

            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.phonepe));
                intent.putExtra(getString(R.string.user_data), userData);
                intent.putExtra(getString(R.string.withdraw_amount), withdrawAmount);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be ₹100");

            }

        } else if (isGooglePaySelected) {

            double withdrawAmount = Double.parseDouble(binding.googlePayAmount.getText().toString().substring(1));

            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.gpay));
                intent.putExtra(getString(R.string.user_data), userData);
                intent.putExtra(getString(R.string.withdraw_amount), withdrawAmount);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be ₹100");

            }

        } else {
            showSnackBar("Please Select a payment mode to continue");
        }

    }

    private double INRTODollar(double inr) {

        double exchangeRate = 0.014;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String decimalValue = decimalFormat.format(inr * exchangeRate);

        if (decimalValue.endsWith("0")) {
            decimalFormat.applyPattern("#.#");
            decimalValue = decimalFormat.format(inr * exchangeRate);
        }

        return Double.parseDouble(decimalValue);
    }

    private void showSnackBar(String message) {

        Snackbar snackbar = Snackbar.make(binding.layoutWithdraw,
                message,
                Snackbar.LENGTH_SHORT);
        snackbar.show();

        binding.btnContinue.setReversed(true);

    }

}