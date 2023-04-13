package com.aviral.eaa1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aviral.eaa1.R;
import com.aviral.eaa1.databinding.ActivityWithdrawBinding;

public class WithdrawActivity extends AppCompatActivity {

    private ActivityWithdrawBinding binding;

    // Selection Color #FFBC36

    private boolean isPayPalSelected = false,
            isPaytmSelected = false,
            isPhonePeSelected = false,
            isGooglePaySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(getString(R.string.payment_mode), getString(R.string.paypal));
            startActivity(intent);
        } else if (isPaytmSelected) {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(getString(R.string.payment_mode), getString(R.string.paytm));
            startActivity(intent);
        } else if (isPhonePeSelected) {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(getString(R.string.payment_mode), getString(R.string.phonepe));
            startActivity(intent);
        } else if (isGooglePaySelected) {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(getString(R.string.payment_mode), getString(R.string.gpay));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Select a payment mode to continue", Toast.LENGTH_SHORT).show();
        }

    }

}