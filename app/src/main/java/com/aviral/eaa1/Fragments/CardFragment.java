package com.aviral.eaa1.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.ScratchViewBinding;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CardFragment extends Fragment implements IUnityAdsInitializationListener {

    private static final String TAG = "AviralAdsCard";

    public ScratchViewBinding binding;
    private Activity activity;
    int chancesLeft;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private long millisLeft=86400000, endTime;
    private boolean timeRunning;
    MainActivity mainActivity;

    private LoadingDialog hideDialog;
    double reward;

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show(requireActivity(), AdsParameters.rewardedAndroidAdUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.d(TAG, "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.d(TAG, "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.d(TAG, "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.d(TAG, "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.d(TAG, "onUnityAdsShowComplete: " + placementId);

        }
    };

    private void DisplayRewarded(){
        UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
        UnityAds.show(activity, "Rewarded_Android", new UnityAdsShowOptions(), showListener);
    }

    private void DisplayInterstitial(){
        UnityAds.load(AdsParameters.interstitialAndroidAdUnitId, loadListener);
        UnityAds.show(mainActivity, "Interstitial_Android", new UnityAdsShowOptions(), showListener);
    }

    @Override
    public void onInitializationComplete() {
        Log.d(TAG, "onInitializationComplete: Ads Initialization Complete");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d(TAG, "onInitializationFailed: Ads Initialization failed " + message);
    }

    public CardFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ScratchViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        if (activity != null && isAdded()){

            double lower = 0.10;
            double upper = 0.50;

            reward = Math.random() * (upper - lower) + lower;
            binding.points.setText("₹"+String.format(Locale.US, "%.2f", reward));

            UnityAds.initialize(requireActivity().getApplicationContext(),
                    AdsParameters.unityGameID, AdsParameters.testMode, this);

            hideDialog = new LoadingDialog(activity);

            sharedPreferences = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            chancesLeft = sharedPreferences.getInt("clsc", 20);

            binding.chanceLeft.setText(chancesLeft + " Chances left");
            binding.scratchView.setStrokeWidth(20);
            binding.scratchView.setBackground(null);
            binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onRevealed(ScratchView scratchView) {
                    if (chancesLeft == 0){
                        binding.chanceLeft.setText("0 Chance left");
                        Toast.makeText(activity, "No chances left", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        scratchView.reveal();

                        DisplayInterstitial();

                        chancesLeft = chancesLeft -1;
                        editor.putInt("clsc", chancesLeft);
                        editor.apply();

                        binding.chanceLeft.setText(chancesLeft + " Chances left");

                        Dialog dialog = new Dialog(mainActivity);
                        dialog.setContentView(R.layout.layout_won_price_dialog);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        if (dialog.getWindow()!=null){
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        }
                        dialog.show();

                        TextView earnedAmountText = dialog.findViewById(R.id.tv_amount);
                        earnedAmountText.setText("₹"+String.format(Locale.US, "%.2f", reward));
                        ConstraintLayout wonPrice = dialog.findViewById(R.id.wonPrice);
                        wonPrice.setOnClickListener(v -> {

                            scratchView.clear();
                            scratchView.mask();

                            DisplayRewarded();

                            if (dialog.isShowing()){
                                dialog.dismiss();
                            }

                            reward = Math.random() * (upper - lower) + lower;
                            new Handler().postDelayed(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    binding.points.setText("₹"+String.format(Locale.US, "%.2f", reward));
                                }
                            }, 1500);
                            updateUserBalance(reward);
                            if (chancesLeft == 0){
                                millisLeft = 86400000;
                                checkCountdownTimer();
                            }
                        });

                    }
                }

                @Override
                public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

                }
            });
        }
    }
    private void checkCountdownTimer(){
        endTime = millisLeft + System.currentTimeMillis();
        CountDownTimer countDownTimer = new CountDownTimer(millisLeft, 1000) {
            @Override
            public void onTick(long l) {
                binding.chanceLeft.setText(formatTime(l));
                chancesLeft = 0;
            }

            @Override
            public void onFinish() {
                chancesLeft = 20;
                SharedPreferences sharedPreferencesCL = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesCL.edit();
                editor.putInt("clsc", 20);
                editor.apply();
                SharedPreferences sharedPreferencesCLs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                int c = sharedPreferencesCLs.getInt("clsc", 0);
                binding.chanceLeft.setText(chancesLeft + " Chances left");
            }
        };
        countDownTimer.start();
        timeRunning = true;
    }

    private String formatTime(long millis) {
        String output = "";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD + " : " + minutesD + " : " + secondsD;

        return output;
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTimeRunningD", timeRunning);
        editor.putLong("millisLeftD", millisLeft);
        editor.putLong("endTimeD", endTime);
        editor.putInt("clsc", chancesLeft);
        editor.apply();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        millisLeft = sharedPreferences.getLong("millisLeftD", millisLeft);
        timeRunning = sharedPreferences.getBoolean("isTimeRunningD", false);
        chancesLeft = sharedPreferences.getInt("clsc", 20);
        binding.chanceLeft.setText(chancesLeft + " Chances left");
        if (timeRunning){
            endTime = sharedPreferences.getLong("endTimeD", endTime);
            millisLeft = endTime - System.currentTimeMillis();
            if (millisLeft<0){
                millisLeft = 0;
                timeRunning = false;
                chancesLeft = 20;
                binding.chanceLeft.setText("20 Chances left");
                SharedPreferences sharedPreferencesCL = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesCL.edit();
                editor.putInt("clsc", 75);
                editor.apply();
                millisLeft = 86400000;
                checkCountdownTimer();
                SharedPreferences sharedPreferencesCLs = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                int c = sharedPreferencesCLs.getInt("clsc", 0);
                Log.d("clsc", ""+c);
            }
            else{
                checkCountdownTimer();
            }
        }
    }
    private void updateUserBalance(double value){
        hideDialog.show();
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
                                hideDialog.dismiss();
                                mainActivity.get_user_balance();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideDialog.dismiss();
                    }
                });
    }
}
