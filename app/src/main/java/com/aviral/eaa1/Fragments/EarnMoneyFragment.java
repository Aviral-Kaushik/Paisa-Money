package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aviral.eaa1.Activity.WithdrawActivity;
import com.aviral.eaa1.Adapter.OptionsRecyclerViewAdapter;
import com.aviral.eaa1.Models.EarningOptions;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.Utils.RecyclerViewMargin;
import com.aviral.eaa1.databinding.EarnMoneyFragmentBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class EarnMoneyFragment extends Fragment {

    private EarnMoneyFragmentBinding binding;

    private UserData userData;

    private LoadingDialog loadingDialog;

    private int dailyBonusChances, watchVideoChances, collectRewardsChances, goldPointsChances;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = EarnMoneyFragmentBinding.inflate(inflater, container, false);

        binding.earnMoneyScrollView.fullScroll(ScrollView.FOCUS_UP);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        binding.walletBalance.setText(String.format("₹%s", userData.getBalance()));
        binding.btnBalance.setText(String.format("₹%s", userData.getBalance()));

        View view = binding.getRoot();

        binding.userName.setText(userData.getName());

        loadingDialog = new LoadingDialog(requireContext());

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());
        ArrayList<String> links = backendProvider.getAllLinks();

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

//        addChancesToSharedPreferences();

        getChancesFromSharedPreferences();

        fetchUserData();
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

    private void addChancesToSharedPreferences() {

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

        SharedPreferences.Editor dailyBonusEditor = dailyBonus.edit();
        SharedPreferences.Editor collectRewardsEditor = collectRewards.edit();
        SharedPreferences.Editor watchVideosEditor = watchVideos.edit();
        SharedPreferences.Editor goldPointsEditor = goldPoints.edit();

        dailyBonusEditor.putInt(getString(R.string.chances_left), 5);
        collectRewardsEditor.putInt(getString(R.string.chances_left), 5);
        watchVideosEditor.putInt(getString(R.string.chances_left), 5);
        goldPointsEditor.putInt(getString(R.string.chances_left), 5);

        dailyBonusEditor.apply();
        collectRewardsEditor.apply();
        watchVideosEditor.apply();
        goldPointsEditor.apply();

        Log.d("AviralAPI", "addChancesToSharedPreferences: Added all the chances in shared preferences");

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
                new OptionsRecyclerViewAdapter(
                        requireContext(),
                        getParentFragmentManager(),
                        optionsArrayList,
                        userData.getUid(),
                        chances
                );

        RecyclerViewMargin recyclerViewMargin = new RecyclerViewMargin(3);
        binding.optionsRecyclerView.addItemDecoration(recyclerViewMargin);

        binding.optionsRecyclerView.setAdapter(optionsRecyclerViewAdapter);

    }
}
