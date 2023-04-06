package com.aviral.eaa1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.aviral.eaa1.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.payment_mode))) {
            setupViews(intent.getStringExtra(getString(R.string.payment_mode)));
        }
    }

    private void setupViews(String paymentMode) {

        if (paymentMode.equals(getString(R.string.paypal))) {

            binding.editTextId.setHint("Paypal id");

        } else if (paymentMode.equals(getString(R.string.paytm))) {

            binding.editTextId.setHint("Paytm UPI id");

        } else if (paymentMode.equals(getString(R.string.phonepe))) {

            binding.editTextId.setHint("Phonepe UPI id");

        } else if (paymentMode.equals(getString(R.string.gpay))) {

            binding.editTextId.setHint("GPay UPI id");

        }

    }
}