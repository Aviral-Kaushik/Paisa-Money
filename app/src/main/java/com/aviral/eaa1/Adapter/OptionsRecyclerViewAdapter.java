package com.aviral.eaa1.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Fragments.EarnMoneyFragment;
import com.aviral.eaa1.Fragments.OptionChances;
import com.aviral.eaa1.Models.EarningOptions;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.Utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OptionsRecyclerViewAdapter
        extends RecyclerView.Adapter<OptionsRecyclerViewAdapter.ViewHolder>
        implements IUnityAdsInitializationListener {

    private static final String TAG = "AviralAPI";

    private final MainActivity mainActivity;

    private static final String TAG_ADD = "AviralAds";
    private final ArrayList<EarningOptions> optionList;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final String uid;
    private final OptionChances chances;
    private final Activity activity;
    public OptionsRecyclerViewAdapter(MainActivity mainActivity, Context context,
                                      FragmentManager fragmentManager,
                                      ArrayList<EarningOptions> optionList,
                                      String uid,
                                      OptionChances chances,
                                      Activity activity,
                                      Context applicationContext) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.optionList = optionList;
        this.uid = uid;
        this.chances = chances;
        this.activity = activity;

        UnityAds.initialize(applicationContext,
                AdsParameters.unityGameID, AdsParameters.testMode, this);

    }

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(activity, AdsParameters.rewardedAndroidAdUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.d(TAG_ADD, "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.d(TAG_ADD, "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.d(TAG_ADD, "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.d(TAG_ADD, "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.d(TAG_ADD, "onUnityAdsShowComplete: " + placementId);
        }
    };

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
        holder.optionButton.setText(String.format("₹%s",
                roundOfNumber(optionList.get(position).getOptionEarningAmount())
        ));

        Glide.with(holder.itemView.getContext())
                .load(optionList.get(position).getOptionImage())
                .into(holder.optionImage);

        if (optionList.get(position).getChancesLeft() > 0) {

            holder.itemView.setOnClickListener(view -> {

                DisplayRewardedAd(position);

            });
        } else {

            checkForChancesRenewal(holder.chances, holder.optionButton);

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

    @Override
    public void onInitializationComplete() {
        Log.d(TAG_ADD, "onInitializationComplete: Ads Initialization Complete");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d(TAG_ADD, "onInitializationFailed: Ads Initialization failed " + message);
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

                if (chances.getDailyBonusChances() > 1) {

                    dailyBonusEditor.putInt(
                            context.getString(R.string.chances_left),
                            (chances.getDailyBonusChances() - 1)
                    );

                } else {

                    dailyBonusEditor.putInt(
                            context.getString(R.string.chances_left),
                            0
                    );

                    Log.d(TAG, "decrementChances: Adding Time to shared Preferences for Daily Bonus");

                    dailyBonusEditor.putLong(
                            context.getString(R.string.daily_bonus_time),
                            TimeUtils.getCurrentTime()
                    );
                }

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

                if (chances.getCollectRewardsChances() > 1) {

                    collectRewardsEditor.putInt(
                            context.getString(R.string.chances_left),
                            (chances.getCollectRewardsChances() - 1)
                    );

                } else {

                    collectRewardsEditor.putInt(
                            context.getString(R.string.chances_left),
                            0
                    );

                    Log.d(TAG, "decrementChances: Adding Time to shared Preferences for Collect Rewards");


                    collectRewardsEditor.putLong(
                            context.getString(R.string.collect_reward_time),
                            TimeUtils.getCurrentTime()
                    );
                }

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
                if (chances.getWatchVideosChances() > 1) {

                    watchVideosEditor.putInt(
                            context.getString(R.string.chances_left),
                            (chances.getWatchVideosChances() - 1)
                    );

                } else {

                    watchVideosEditor.putInt(
                            context.getString(R.string.chances_left),
                            0
                    );

                    Log.d(TAG, "decrementChances: Adding Time to shared Preferences for Watch Videos");

                    watchVideosEditor.putLong(
                            context.getString(R.string.watch_video_time),
                            TimeUtils.getCurrentTime()
                    );
                }

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
                if (chances.getGoldPointsChances() > 1) {

                    goldPointsEditor.putInt(
                            context.getString(R.string.chances_left),
                            (chances.getGoldPointsChances() - 1)
                    );

                } else {

                    goldPointsEditor.putInt(
                            context.getString(R.string.chances_left),
                            0
                    );

                    Log.d(TAG, "decrementChances: Adding Time to shared Preferences for Gold Points");

                    goldPointsEditor.putLong(
                            context.getString(R.string.gold_points_time),
                            TimeUtils.getCurrentTime()
                    );

                }


                goldPointsEditor.apply();

                Log.d(TAG, "decrementChances: goldPoints chances--");
                break;

        }

    }

    private void checkForChancesRenewal(TextView optionChances,
                                        TextView optionAmount) {

        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.show();

        if (chances.getDailyBonusChances() == 0) {

            SharedPreferences dailyBonus = context.getSharedPreferences(
                    context.getString(R.string.reward_name_daily_bonus),
                    Context.MODE_PRIVATE
            );

            long storedTime = dailyBonus.getLong(context.getString(R.string.daily_bonus_time), -1);

            boolean comparison = TimeUtils.compareTimeWithSixHours(storedTime, "dailyBonus");

            if (comparison) {

                SharedPreferences.Editor dailyBonusEditor = dailyBonus.edit();

                dailyBonusEditor.putInt(
                        context.getString(R.string.chances_left),
                        5
                );

                dailyBonusEditor.apply();

                toggleView(optionChances, optionAmount);
            }

        }

        if (chances.getCollectRewardsChances() == 0) {

            SharedPreferences collectRewards = context.getSharedPreferences(
                    context.getString(R.string.reward_name_collect_rewards),
                    Context.MODE_PRIVATE
            );

            long storedTime = collectRewards.getLong(context.getString(R.string.collect_reward_time), -1);

            boolean comparison = TimeUtils.compareTimeWithSixHours(storedTime, "collectRewards");

            if (comparison) {

                SharedPreferences.Editor collectRewardsBonus = collectRewards.edit();

                collectRewardsBonus.putInt(
                        context.getString(R.string.chances_left),
                        5
                );

                collectRewardsBonus.apply();

                toggleView(optionChances, optionAmount);
            }

        }

        if (chances.getWatchVideosChances() == 0) {

            SharedPreferences watchVideos = context.getSharedPreferences(
                    context.getString(R.string.reward_name_watch_videos),
                    Context.MODE_PRIVATE
            );

            long storedTime = watchVideos.getLong(context.getString(R.string.watch_video_time), -1);

            boolean comparison = TimeUtils.compareTimeWithSixHours(storedTime, "watchVideos");

            if (comparison) {

                SharedPreferences.Editor watchVideoEditor = watchVideos.edit();

                watchVideoEditor.putInt(
                        context.getString(R.string.chances_left),
                        5
                );

                watchVideoEditor.apply();

                toggleView(optionChances, optionAmount);
            }

        }

        if (chances.getGoldPointsChances() == 0) {

            SharedPreferences goldPoints = context.getSharedPreferences(
                    context.getString(R.string.reward_name_gold_points),
                    Context.MODE_PRIVATE
            );

            long storedTime = goldPoints.getLong(context.getString(R.string.gold_points_time), -1);

            boolean comparison = TimeUtils.compareTimeWithSixHours(storedTime, "goldPoints");

            if (comparison) {

                SharedPreferences.Editor goldPointsEditor = goldPoints.edit();

                goldPointsEditor.putInt(
                        context.getString(R.string.chances_left),
                        5
                );

                goldPointsEditor.apply();

                toggleView(optionChances, optionAmount);
            }

        }

        loadingDialog.dismiss();

    }

    private void toggleView(TextView optionChances, TextView optionAmount) {

        optionChances.setTextColor(Color.parseColor("#FFBC36"));
        optionChances.setText(context.getString(R.string.max_chances_left));

        optionAmount.setBackground(AppCompatResources.getDrawable(context, R.drawable.text_expandable_bg));

    }

    private String roundOfNumber(double number) {

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        String decimalValue = decimalFormat.format(number);

        if (decimalValue.length() == 3) {
            decimalValue += "0";
        }

        return decimalValue;
    }

    public void DisplayRewardedAd(int position) {
        UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
        UnityAds.show(activity, "Rewarded_Android", new UnityAdsShowOptions(), showListener);

        decrementChances(optionList.get(position).getRewardName());

        updateUserBalanceAndText(optionList.get(position).getOptionEarningAmount());

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_won_price_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (dialog.getWindow()!=null){
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        TextView earnedAmountText = dialog.findViewById(R.id.tv_amount);
        earnedAmountText.setText(String.format("₹%s", optionList.get(position).getOptionEarningAmount()));
        ConstraintLayout wonPrice = dialog.findViewById(R.id.wonPrice);
        wonPrice.setOnClickListener(v -> {
            DisplayInterstitial();
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        });
    }

    private void DisplayInterstitial(){
        UnityAds.load(AdsParameters.interstitialAndroidAdUnitId, loadListener);
        UnityAds.show(mainActivity, "Interstitial_Android", new UnityAdsShowOptions(), showListener);
    }

    private void updateUserBalanceAndText(double value){
        String uid = activity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", "");
        String balance_add = String.valueOf(value);
        AndroidNetworking.post(Links.UPDATE_USER_BALANCE)
                .addBodyParameter("user_id", uid)
                .addBodyParameter("value", balance_add)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("updated")){
                                mainActivity.get_user_balance();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

}
