package com.aviral.eaa1.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.databinding.ActivityWithdrawBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WithdrawActivity extends AppCompatActivity {

    private ActivityWithdrawBinding binding;

    // Selection Color #FFBC36

    private boolean isPayPalSelected = false,
            isPaytmSelected = false,
            isPhonePeSelected = false,
            isGooglePaySelected = false;


    float balance;

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        get_user_balance();



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
                intent.putExtra(getString(R.string.withdraw_amount), 1.4);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be $1.4");

            }


        } else if (isPaytmSelected) {

            double withdrawAmount = Double.parseDouble(binding.paytmAmount.getText().toString().substring(1));

            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.paytm));
                intent.putExtra(getString(R.string.withdraw_amount), 100);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be ₹100");

            }


        } else if (isPhonePeSelected) {

            double withdrawAmount = Double.parseDouble(binding.phonePeAmount.getText().toString().substring(1));


            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.phonepe));
                intent.putExtra(getString(R.string.withdraw_amount), 100);
                startActivity(intent);

            } else {

                showSnackBar("Minimum Withdraw amount has to be ₹100");

            }


        } else if (isGooglePaySelected) {

            double withdrawAmount = Double.parseDouble(binding.googlePayAmount.getText().toString().substring(1));


            if (withdrawAmount > 100) {

                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(getString(R.string.payment_mode), getString(R.string.gpay));
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

    public void get_user_balance(){
        String uid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", "");
        AndroidNetworking.post(Links.GET_USER_COINS)
                .addBodyParameter("user_id", uid)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            float balance = (float) response.getDouble("balance");
                            setBalance(balance);
                            binding.amount.setText(String.format("₹%s ≈ $%s",
                                    getBalance(),
                                    INRTODollar(getBalance())));

                            binding.userName.setText(getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));
                            binding.tvUserName.setText(getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));

                            binding.payapalAmount.setText(String.format("$%s", INRTODollar(getBalance())));
                            binding.paytmAmount.setText(String.format("₹%s", getBalance()));
                            binding.phonePeAmount.setText(String.format("₹%s", getBalance()));
                            binding.googlePayAmount.setText(String.format("₹%s", getBalance()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}