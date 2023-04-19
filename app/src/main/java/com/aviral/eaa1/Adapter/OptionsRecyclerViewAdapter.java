package com.aviral.eaa1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Fragments.OptionChances;
import com.aviral.eaa1.Models.EarningOptions;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OptionsRecyclerViewAdapter
        extends RecyclerView.Adapter<OptionsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "AviralAPI";

    private final ArrayList<EarningOptions> optionList;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final String uid;
    private final OptionChances chances;

    public OptionsRecyclerViewAdapter(Context context,
                                      FragmentManager fragmentManager,
                                      ArrayList<EarningOptions> optionList,
                                      String uid,
                                      OptionChances chances) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.optionList = optionList;
        this.uid = uid;
        this.chances = chances;
    }

    @NonNull
    @Override
    public OptionsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_layout_options,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsRecyclerViewAdapter.ViewHolder holder, int position) {

        setAnimation(holder.itemView, holder.itemView.getContext());

        if (optionList.get(position).getChancesLeft() <= 0) {
            holder.chances.setTextColor(holder.itemView.getContext()
                    .getResources().getColor(R.color.chances_not_left));

            holder.optionButton.setBackground(AppCompatResources.getDrawable(
                    holder.itemView.getContext(),
                    R.drawable.option_price_button_backround_grey));

            holder.chances.setText(R.string.reward_completed);

        } else {
            holder.chances.setText(String.format("%d Chance left", optionList.get(position).getChancesLeft()));
        }

        holder.optionTitle.setText(optionList.get(position).getOptionTitle());
        holder.optionButton.setText(String.format("₹%s", optionList.get(position).getOptionEarningAmount()));

        Glide.with(holder.itemView.getContext())
                .load(optionList.get(position).getOptionImage())
                .into(holder.optionImage);

        if (optionList.get(position).getChancesLeft() > 0) {

            holder.itemView.setOnClickListener(view -> {
//              handle url launch here
//              openUrl(optionList.get(position).getLink());

                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
                        "₹" + optionList.get(position).getOptionEarningAmount()
                );

                updateUserBalance(
                        optionList.get(position).getRewardName(),
                        String.valueOf(optionList.get(position).getOptionEarningAmount())
                );

                wonPriceClaimDialog.show(fragmentManager, "Earned Amount");

            });
        }

    }

    private void setAnimation(View itemView, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

        itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView optionImage;
        private final TextView optionTitle;
        private final TextView chances;
        private final TextView optionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionImage = itemView.findViewById(R.id.option_image);
            optionTitle = itemView.findViewById(R.id.option_title);
            chances = itemView.findViewById(R.id.chances);
            optionButton = itemView.findViewById(R.id.price_button);

        }
    }

    private void updateUserBalance(String rewardName, String earnedAmount) {

        Log.d(TAG, "updateUserBalance: Updating User Balance");

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        ApiBackendProvider backendProvider = new ApiBackendProvider(context);

        backendProvider.updateUserBalance(
                uid,
                String.valueOf(earnedAmount)
        );

        Log.d(TAG, "updateUserBalance: Updated User Balance");

        decrementChances(rewardName);

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }, 2000);

    }

    private void decrementChances(String rewardName) {

        Log.d(TAG, "decrementChances: Decrementing Chances");

        switch (rewardName) {

            case "dailyBonus":

                Log.d(TAG, "decrementChances: Opening SP of dailyBonus");

                SharedPreferences dailyBonus = context.getSharedPreferences(
                        context.getString(R.string.reward_name_daily_bonus),
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor dailyBonusEditor = dailyBonus.edit();
                dailyBonusEditor.putInt(
                        context.getString(R.string.chances_left),
                        (chances.getDailyBonusChances() - 1)
                );
                dailyBonusEditor.apply();

                Log.d(TAG, "decrementChances: dailyBonus chances--");
                break;

            case "collectRewards":

                Log.d(TAG, "decrementChances: Opening SP of collectRewards");

                SharedPreferences collectRewards = context.getSharedPreferences(
                        context.getString(R.string.reward_name_collect_rewards),
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor collectRewardsEditor = collectRewards.edit();
                collectRewardsEditor.putInt(
                        context.getString(R.string.chances_left),
                        (chances.getCollectRewardsChances() - 1)
                );
                collectRewardsEditor.apply();

                Log.d(TAG, "decrementChances: collectRewards chances--");
                break;

            case "watchVideos":

                Log.d(TAG, "decrementChances: Opening SP of watchRewards");

                SharedPreferences watchVideos = context.getSharedPreferences(
                        context.getString(R.string.reward_name_watch_videos),
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor watchVideosEditor = watchVideos.edit();
                watchVideosEditor.putInt(
                        context.getString(R.string.chances_left),
                        (chances.getWatchVideosChances() - 1)
                );
                watchVideosEditor.apply();

                Log.d(TAG, "decrementChances: watchRewards chances--");
                break;

            case "goldPoints":

                Log.d(TAG, "decrementChances: Opening SP of goldPoints");

                SharedPreferences goldPoints = context.getSharedPreferences(
                        context.getString(R.string.reward_name_gold_points),
                        Context.MODE_PRIVATE
                );
                SharedPreferences.Editor goldPointsEditor = goldPoints.edit();
                goldPointsEditor.putInt(
                        context.getString(R.string.chances_left),
                        (chances.getGoldPointsChances() - 1)
                );
                goldPointsEditor.apply();

                Log.d(TAG, "decrementChances: goldPoints chances--");
                break;

        }

    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        context.startActivity(intent);
    }
}
