package com.aviral.eaa1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aviral.eaa1.InviteActivity;
import com.aviral.eaa1.R;
import com.aviral.eaa1.ReferralEarningActivity;
import com.aviral.eaa1.WithdrawActivity;
import com.aviral.eaa1.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        binding.btnInviteEarn.setOnClickListener(view1 ->
                startActivity(new Intent(getContext(), InviteActivity.class)));

        binding.btnReferralEarning.setOnClickListener(view1 ->
                startActivity(new Intent(getContext(), ReferralEarningActivity.class)));

        binding.buttonWithdraw.setOnClickListener(view1 ->
                startActivity(new Intent(getContext(), WithdrawActivity.class)));

        // Inflate the layout for this fragment
        return view;
    }
}