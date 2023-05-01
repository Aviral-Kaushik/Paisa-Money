package com.aviral.eaa1.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentSpinBinding;
import com.google.android.material.snackbar.Snackbar;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class SpinFragment extends Fragment implements IUnityAdsInitializationListener {

    private MainActivity mainActivity;

    public SpinFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    private Activity activity;

    private static final String TAG = "AviralAds";

    public FragmentSpinBinding binding;

    final double[] sectors = {0.20, 0.75, 0.05, 0.25, 0.15, 1.00, 0.10, 0.50};
    final int[] sectorsDegree = new int[sectors.length];

    int randomSectorIndex = 0;
    boolean spinning = false;

    Random random = new Random();


    private int chancesLeft;

    private LoadingDialog loadingDialog;

    private ApiBackendProvider backendProvider;

    private double earnedAmount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSpinBinding.inflate(inflater, container, false);




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        UnityAds.initialize(activity.getApplicationContext(),
                AdsParameters.unityGameID, AdsParameters.testMode, this);

        backendProvider = new ApiBackendProvider(requireContext());

        loadingDialog = new LoadingDialog(requireContext());


        getChances();

        binding.btnSpin.setOnSlideCompleteListener(slideToActView -> startSpin());
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

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
//            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
//                spinning = false;
//
//                decrementChances();
//
//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                        "₹" + earnedAmount,
//                        getString(R.string.spin_fragment),
//                        activity);
//
//                updateUserBalance(earnedAmount);
//
//                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
//
//            }
        }
    };

    @Override
    public void onInitializationComplete() {
        Log.d(TAG, "onInitializationComplete: Ads Initialization Complete");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d(TAG, "onInitializationFailed: Ads Initialization failed " + message);
    }

    private void getChances() {
        SharedPreferences chancesPreferences = requireContext().getSharedPreferences("spinChances", Context.MODE_PRIVATE);
        chancesLeft = chancesPreferences.getInt("chancesLeft", 10);

        if (chancesLeft > 1) {
            binding.tvChances.setText(chancesLeft + " Chances Left");
        } else {
            binding.tvChances.setText(chancesLeft + " Chance Left");
        }

        loadingDialog.dismiss();

    }


    private void startSpin() {
        generateSectorDegree();

        if (chancesLeft > 0) {
            if (!spinning) {
                spin();
                spinning = true;
            }
        } else {
            Snackbar snackbar = Snackbar.make(binding.spinLayout,
                    "You Cannot Spin as 0 Chances Left",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void spin() {
        randomSectorIndex = random.nextInt(sectors.length);

        int randomDegree = generateRandomDegreeToSpin();

        RotateAnimation rotateAnimation = new RotateAnimation(0, randomDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);

        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                earnedAmount = sectors[sectors.length - (randomSectorIndex + 1)];

                DisplayRewardedAd();

//                 Save Earned Amount in Dialog Fragment
//                spinning = false;

//                decrementChances();


                binding.balance.setText(String.format("₹%s",
                        Double.parseDouble(String.valueOf(mainActivity.getBalance()))
                                + earnedAmount));

//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                        "₹" + earnedAmount,
//                        getString(R.string.spin_fragment),
//                        userData);
//
//                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");

//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog("₹" + earnedAmount);
//
//                updateUserBalance(earnedAmount);
//
//                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.spinningWheel.startAnimation(rotateAnimation);

    }



    private void decrementChances() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("spinChances", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (chancesLeft > 0) {
            editor.putInt("chancesLeft", (chancesLeft - 1));
            editor.apply();
        }

    }

    private int generateRandomDegreeToSpin() {
        return (360 * sectors.length) + sectorsDegree[randomSectorIndex] + 10;
    }

    private void generateSectorDegree() {
        int sectorDegree = 360 / sectors.length;

        for (int i = 0; i < sectors.length; i++) {
            sectorsDegree[i] = (i + 1) * sectorDegree;
        }
    }

    public void DisplayRewardedAd() {
        UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
        UnityAds.show(activity, "Rewarded_Android", new UnityAdsShowOptions(), showListener);


//        new Handler().postDelayed(() -> {
//
//            spinning = false;
//
//            decrementChances();
//
//            WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                    "₹" + earnedAmount,
//                    getString(R.string.spin_fragment),
//                    activity);
//
//            updateUserBalance(earnedAmount);
//
//            wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
//        }, 1000);
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_won_price_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (dialog.getWindow()!=null){
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        TextView earnedAmountText = dialog.findViewById(R.id.tv_amount);
        earnedAmountText.setText("₹"+earnedAmount);
        ConstraintLayout wonPrice = dialog.findViewById(R.id.wonPrice);
        wonPrice.setOnClickListener(v -> {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
            UnityAds.show(activity, "Interstitial_Android", new UnityAdsShowOptions(), showListener);
            updateUserBalance(earnedAmount);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUserBalance(double value){
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