package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aviral.eaa1.Activity.InviteActivity;
import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Activity.PrivacyPolicy;
import com.aviral.eaa1.Activity.ReferralEarningActivity;
import com.aviral.eaa1.Activity.TermsAndCondition;
import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {


    private final MainActivity mainActivity;

    public ProfileFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.userName.setText(mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));
        binding.userEmail.setText(mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("email", ""));

        binding.btnInviteEarn.setOnClickListener(view1 ->{
            Intent intent = new Intent(getContext(), InviteActivity.class);
            startActivity(intent);
        });

        binding.btnReferralEarning.setOnClickListener(view1 ->{
            Intent intent = new Intent(getContext(), ReferralEarningActivity.class);
            startActivity(intent);
        });

        binding.buttonWithdraw.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), WithdrawActivity.class);
            startActivity(intent);
        });
        binding.btnPrivacyPolicy.setOnClickListener(v -> startActivity(new Intent(mainActivity, PrivacyPolicy.class)));
        binding.btnTerms.setOnClickListener(v -> startActivity(new Intent(mainActivity, TermsAndCondition.class)));
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}