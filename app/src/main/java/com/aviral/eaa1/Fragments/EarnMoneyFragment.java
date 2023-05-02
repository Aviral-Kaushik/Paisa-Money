package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Models.EarningOptions;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.databinding.EarnMoneyFragmentBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class EarnMoneyFragment extends Fragment {

    private final MainActivity mainActivity;

    public EarnMoneyFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public EarnMoneyFragmentBinding binding;

    private int dailyBonusChances, watchVideoChances, collectRewardsChances, goldPointsChances;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EarnMoneyFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.earnMoneyScrollView.fullScroll(ScrollView.FOCUS_UP);

        binding.userName.setText(mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("name", ""));

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());
        ArrayList<String> links = backendProvider.getAllLinks();

        binding.ludo.setOnClickListener(view1 -> openUrl(links.get(1)));
        binding.chess.setOnClickListener(view1 -> openUrl(links.get(2)));
        binding.pubg.setOnClickListener(view1 -> openUrl(links.get(3)));
        binding.puzzle.setOnClickListener(view1 -> openUrl(links.get(4)));
        binding.card.setOnClickListener(view1 -> openUrl(links.get(5)));

        binding.buttonWithdraw.setOnClickListener(view1 -> {

            Intent intent = new Intent(getContext(), WithdrawActivity.class);

            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        getChancesFromSharedPreferences();

    }

    private void getChancesFromSharedPreferences() {

        SharedPreferences dailyBonus = requireActivity().getSharedPreferences(
                requireContext().getString(R.string.reward_name_daily_bonus),
                Context.MODE_PRIVATE
        );

        SharedPreferences collectRewards = requireActivity().getSharedPreferences(
                requireContext().getString(R.string.reward_name_collect_rewards),
                Context.MODE_PRIVATE
        );

        SharedPreferences watchVideos = requireActivity().getSharedPreferences(
                requireContext().getString(R.string.reward_name_watch_videos),
                Context.MODE_PRIVATE
        );

        SharedPreferences goldPoints = requireActivity().getSharedPreferences(
                requireContext().getString(R.string.reward_name_gold_points),
                Context.MODE_PRIVATE
        );

        dailyBonusChances = dailyBonus.getInt(getString(R.string.chances_left), 0);
        collectRewardsChances = collectRewards.getInt(getString(R.string.chances_left), 0);
        watchVideoChances = watchVideos.getInt(getString(R.string.chances_left), 0);
        goldPointsChances = goldPoints.getInt(getString(R.string.chances_left), 0);

        Log.d("AviralAPI", "setChances: dailyBonusChances " + dailyBonusChances);
        Log.d("AviralAPI", "setChances: collectRewardsChances " + collectRewardsChances);
        Log.d("AviralAPI", "setChances: watchVideoChances " + watchVideoChances);
        Log.d("AviralAPI", "setChances: goldPointsChances " + goldPointsChances);

        setUpOptionAdapter();
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

        OptionChances chances = new OptionChances(
                dailyBonusChances,
                collectRewardsChances,
                watchVideoChances,
                goldPointsChances
        );

        ArrayList<EarningOptions> optionsArrayList = new ArrayList<>();

        optionsArrayList.add(new EarningOptions(
                R.drawable.ic_daily,
                getString(R.string.daily_bonus),
                5,
                dailyBonusChances,
                0.25,
                Calendar.getInstance(),
                "links.get(0)",
                getString(R.string.reward_name_daily_bonus)
        ));

        optionsArrayList.add(new EarningOptions(
                R.drawable.ic_collect,
                getString(R.string.collect_rewards),
                5,
                collectRewardsChances,
                0.20,
                Calendar.getInstance(),
                "links.get(0),",
                getString(R.string.reward_name_collect_rewards)
        ));

        optionsArrayList.add(new EarningOptions(
                R.drawable.ic_watch_videos,
                getString(R.string.watch_videos),
                5,
                watchVideoChances,
                0.50,
                Calendar.getInstance(),
                "links.get(0)",
                getString(R.string.reward_name_watch_videos)
        ));

        optionsArrayList.add(new EarningOptions(
                R.drawable.ic_gold_points,
                getString(R.string.gold_points),
                5,
                goldPointsChances,
                0.50,
                Calendar.getInstance(),
                "links.get(0)",
                getString(R.string.reward_name_gold_points)
        ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        binding.optionsRecyclerView.setLayoutManager(linearLayoutManager);

        OptionsRecyclerViewAdapter optionsRecyclerViewAdapter =
                new OptionsRecyclerViewAdapter(mainActivity,
                        requireContext(),
                        getParentFragmentManager(),
                        optionsArrayList,
                        mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", ""),
                        chances,
                        requireActivity(),
                        requireActivity().getApplicationContext()
                );

        RecyclerViewMargin recyclerViewMargin = new RecyclerViewMargin(1);
        binding.optionsRecyclerView.addItemDecoration(recyclerViewMargin);

        binding.optionsRecyclerView.setAdapter(optionsRecyclerViewAdapter);

    }
}
