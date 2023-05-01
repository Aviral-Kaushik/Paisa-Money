package com.aviral.eaa1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.databinding.ActivityInviteBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class InviteActivity extends AppCompatActivity {

    private ActivityInviteBinding binding;
    String referral_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        referral_code = getSharedPreferences("user", Context.MODE_PRIVATE).getString("referral_code", "");

        binding.referralCode.setText(referral_code);

        binding.shareWhatsapp.setOnClickListener(view -> sendWhatsappMessage());
        binding.shareTelegram.setOnClickListener(view -> sendTelegramMessage());
        binding.share.setOnClickListener(view -> shareIntent());

        binding.copy.setOnClickListener(view -> copyReferralCodeToClipboard());
        binding.copyText.setOnClickListener(view -> copyReferralCodeToClipboard());
        get_user_balance();
    }


    private String getShareMessage() {
        return "Paisamoney is an earning app that allows you to make money simply by completing tasks and referring your friends. With Paisamoney, you can earn real cash and withdraw your earnings via multiple payment channel.\n" +
                "\n" +
                "The best part is, if you sign up using my referral code " + referral_code + ", you'll receive a bonus to start earning right away!\n" +
                "\n" +
                "I highly recommend giving Paisamoney a try – it's a fun and easy way to earn some extra income. And with my referral code, you'll have a head start!";
    }

    private void sendWhatsappMessage() {
        String url = "https://api.whatsapp.com/send?text=" + getShareMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    private void sendTelegramMessage() {
        String url = "tg://msg?text=" + getShareMessage();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void shareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getShareMessage());
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void copyReferralCodeToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Referral Code", binding.referralCode.getText().toString());
        clipboard.setPrimaryClip(clip);

        Snackbar snackbar = Snackbar.make(binding.inviteLayout
                , "Referral Code Copied to Clipboard",
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void get_user_balance(){
        binding.btnBalance.setText("----");
        String uid = getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", "");
        AndroidNetworking.post(Links.GET_USER_COINS)
                .addBodyParameter("user_id", uid)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float balance = (float) response.getDouble("balance");
                            String balance_text = "₹"+String.format(Locale.US, "%.2f", balance);
                            binding.btnBalance.setText(balance_text);
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