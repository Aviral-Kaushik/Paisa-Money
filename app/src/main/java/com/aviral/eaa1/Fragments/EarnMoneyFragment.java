package com.aviral.eaa1.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Models.Options;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.databinding.EarnMoneyFragmentBinding;

import java.util.ArrayList;

public class EarnMoneyFragment extends Fragment {

    private EarnMoneyFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EarnMoneyFragmentBinding.inflate(inflater, container, false);

        binding.earnMoneyScrollView.fullScroll(ScrollView.FOCUS_UP);

        View view = binding.getRoot();

        setUpOptionAdapter();

        binding.buttonWithdraw.setOnClickListener(view1 ->
                startActivity(new Intent(getContext(), WithdrawActivity.class)));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpOptionAdapter() {
        ArrayList<Options> optionsArrayList = new ArrayList<>();

        optionsArrayList.add(new Options(
                R.drawable.ic_rate,
                "Rate 5 Star & Earn Money",
                1,
                "₹10.00"
        ));

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
