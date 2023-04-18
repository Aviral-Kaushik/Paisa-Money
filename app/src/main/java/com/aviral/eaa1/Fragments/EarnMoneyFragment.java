package com.aviral.eaa1.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Models.Options;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.databinding.EarnMoneyFragmentBinding;

import java.util.ArrayList;

public class EarnMoneyFragment extends Fragment {

    private EarnMoneyFragmentBinding binding;

    private UserData userData;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EarnMoneyFragmentBinding.inflate(inflater, container, false);

        binding.earnMoneyScrollView.fullScroll(ScrollView.FOCUS_UP);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

//        ApiBackendProviderkendProvider backendProvider = new ApiBackendProvider(requireContext());
//
//        UserData updatedUseData = backendProvider.fetchUserData(userData.getEmail());
//
//        new Handler().postDelayed(() -> {
//
//            binding.walletBalance.setText(String.format("₹%s", updatedUseData.getBalance()));
//            binding.btnBalance.setText(String.format("₹%s", updatedUseData.getBalance()));
//
//        }, 2000);

        binding.walletBalance.setText(String.format("₹%s", userData.getBalance()));
        binding.btnBalance.setText(String.format("₹%s", userData.getBalance()));

        View view = binding.getRoot();

        binding.userName.setText(userData.getName());

        setUpOptionAdapter();

        ArrayList<String> links;

        loadingDialog = new LoadingDialog(requireContext());

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());
        links = backendProvider.getAllLinks();

        ArrayList<String> finalLinks = links;
        binding.ludo.setOnClickListener(view1 -> openUrl(finalLinks.get(1)));
        binding.chess.setOnClickListener(view1 -> openUrl(finalLinks.get(2)));
        binding.pubg.setOnClickListener(view1 -> openUrl(finalLinks.get(3)));
        binding.puzzle.setOnClickListener(view1 -> openUrl(finalLinks.get(4)));
        binding.card.setOnClickListener(view1 -> openUrl(finalLinks.get(5)));

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

            binding.walletBalance.setText(String.format("₹%s", updatedUserData.getBalance()));
            binding.btnBalance.setText(String.format("₹%s", updatedUserData.getBalance()));
        }, 2000);


        userData = updatedUserData;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        startActivity(intent);
    }

    private void setUpOptionAdapter() {
        ArrayList<Options> optionsArrayList = new ArrayList<>();

        optionsArrayList.add(new Options(
                R.drawable.ic_daily,
                "Daily Bonus",
                4,
                "₹0.25"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_collect,
                "Collect Rewards",
                0,
                "₹0.20"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_watch_videos,
                "Watch Videos",
                10,
                "₹0.50"
        ));

        optionsArrayList.add(new Options(
                R.drawable.ic_gold_points,
                "Gold Points",
                1,
                "₹0.50"
        ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        binding.optionsRecyclerView.setLayoutManager(linearLayoutManager);

        OptionsRecyclerViewAdapter optionsRecyclerViewAdapter =
                new OptionsRecyclerViewAdapter(optionsArrayList);

        RecyclerViewMargin recyclerViewMargin = new RecyclerViewMargin(3);
        binding.optionsRecyclerView.addItemDecoration(recyclerViewMargin);

        binding.optionsRecyclerView.setAdapter(optionsRecyclerViewAdapter);

    }
}
