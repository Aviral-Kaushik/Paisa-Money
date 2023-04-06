package com.aviral.eaa1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import com.aviral.eaa1.databinding.ActivityInviteBinding;

public class InviteActivity extends AppCompatActivity {

    private ActivityInviteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.copy.setOnClickListener(view -> copyReferralCodeToClipboard());
        binding.copyText.setOnClickListener(view -> copyReferralCodeToClipboard());
    }

    private void copyReferralCodeToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Referral Code", binding.referralCode.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
}