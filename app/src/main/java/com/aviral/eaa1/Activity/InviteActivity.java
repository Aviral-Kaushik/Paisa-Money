package com.aviral.eaa1.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.databinding.ActivityInviteBinding;
import com.google.android.material.snackbar.Snackbar;

public class InviteActivity extends AppCompatActivity {

    private ActivityInviteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserData userData = new UserData();

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.user_data))) {
            userData = intent.getParcelableExtra(getString(R.string.user_data));
        }

        binding.referralCode.setText(userData.getReferralCode());
        binding.btnBalance.setText(userData.getBalance());

        binding.copy.setOnClickListener(view -> copyReferralCodeToClipboard());
        binding.copyText.setOnClickListener(view -> copyReferralCodeToClipboard());
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
}