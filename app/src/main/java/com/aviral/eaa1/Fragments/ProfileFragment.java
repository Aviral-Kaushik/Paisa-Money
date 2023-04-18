package com.aviral.eaa1.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aviral.eaa1.Activity.InviteActivity;
import com.aviral.eaa1.Activity.ReferralEarningActivity;
import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private UserData userData;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());

        binding.walletBalance.setText(String.format("₹%s", userData.getBalance()));
        binding.btnBalance.setText(String.format("₹%s", userData.getBalance()));

        binding.userName.setText(userData.getName());
        binding.userEmail.setText(userData.getEmail());

        binding.btnInviteEarn.setOnClickListener(view1 ->{
            Intent intent = new Intent(getContext(), InviteActivity.class);
            intent.putExtra(getString(R.string.user_data), userData);
            startActivity(intent);
        });

        binding.btnReferralEarning.setOnClickListener(view1 ->{
            Intent intent = new Intent(getContext(), ReferralEarningActivity.class);
            intent.putExtra(getString(R.string.user_data), userData);
            startActivity(intent);
        });

        binding.buttonWithdraw.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), WithdrawActivity.class);
            intent.putExtra(getString(R.string.user_data), userData);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadingDialog.show();

        fetchUserData();
    }

    private void fetchUserData() {

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());

        UserData updatedUserData = backendProvider.fetchUserData(userData.getEmail());

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();
            binding.btnBalance.setText(String.format("₹%s", updatedUserData.getBalance()));
        }, 2000);

        userData = updatedUserData;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}