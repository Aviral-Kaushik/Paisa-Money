package com.aviral.eaa1.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Activity.MainActivity;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.Links;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class ScratchCardFragment extends Fragment implements IUnityAdsInitializationListener {
    private MainActivity mainActivity;

    public ScratchCardFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    private static final String TAG = "AviralAds";

    public FragmentScratchCardBinding binding;


    private int chancesLeft;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UnityAds.initialize(requireActivity().getApplicationContext(),
                AdsParameters.unityGameID, AdsParameters.testMode, this);

        loadingDialog = new LoadingDialog(requireContext());

        getChances();

        binding.scratchCard.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.setVisibility(View.GONE);

                DisplayRewardedAd();

//                decrementChances();
//
//                updateUserBalance(binding.amountWon.getText().toString().substring(1));
//
                binding.btnBalance.setText(String.format("₹%s",
                        Double.parseDouble(String.valueOf(mainActivity.getBalance()))
                                + Double.parseDouble(binding.amountWon.getText().toString().substring(1))));

//                updateUserBalance(binding.amountWon.getText().toString().substring(1));
//
//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(binding.amountWon.getText().toString());
//
//                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");

            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });
    }

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
//            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
//                decrementChances();
//
//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                        binding.amountWon.getText().toString(),
//                        getString(R.string.scratch_fragment),
//                        requireActivity());
//
//                updateUserBalance(binding.amountWon.getText().toString().substring(1));
//
//                wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
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

    @Override
    public void onStart() {
        super.onStart();


    }




    private void getChances() {
        SharedPreferences chancesPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        chancesLeft = chancesPreferences.getInt("chancesLeft", 10);

        if (chancesLeft > 1) {
            binding.tvChances.setText(chancesLeft + " Chances Left");
        } else if (chancesLeft == 1) {
            binding.tvChances.setText(chancesLeft + " Chance Left");
        } else {
            binding.tvChances.setText(chancesLeft + " Chance Left");

            binding.scratchCard.setVisibility(View.GONE);
            binding.scratchImage.setVisibility(View.VISIBLE);


            Toast.makeText(requireContext(), "0 Chances Left", Toast.LENGTH_SHORT).show();
        }

        loadingDialog.dismiss();

    }

    private void decrementChances() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("scratchChances", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (chancesLeft > 0) {
            editor.putInt("chancesLeft", (chancesLeft - 1));
            editor.apply();
        }

    }

    private double generateRandomNumber() {
        double min = 0.10;
        double max = 0.50;

        Random random = new Random();

        double randomDouble = min + (max - min) * random.nextDouble();

        return Math.round(randomDouble * 100.0) / 100.0;
    }

    @SuppressLint("SetTextI18n")
    public void DisplayRewardedAd() {
        UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
        UnityAds.show(mainActivity, "Rewarded_Android", new UnityAdsShowOptions(), showListener);

        decrementChances();

//        WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                binding.amountWon.getText().toString(),
//                getString(R.string.scratch_fragment),
//                requireActivity());
//
//        updateUserBalance(Double.parseDouble(binding.amountWon.getText().toString().substring(1)));
//
//        wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
        Dialog dialog = new Dialog(mainActivity);
        dialog.setContentView(R.layout.layout_won_price_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (dialog.getWindow()!=null){
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.show();
        TextView earnedAmountText = dialog.findViewById(R.id.tv_amount);
        earnedAmountText.setText("₹"+(Double.parseDouble(binding.amountWon.getText().toString().substring(1))));
        ConstraintLayout wonPrice = dialog.findViewById(R.id.wonPrice);
        wonPrice.setOnClickListener(v -> {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);
            UnityAds.show(mainActivity, "Interstitial_Android", new UnityAdsShowOptions(), showListener);
            updateUserBalance(Double.parseDouble(binding.amountWon.getText().toString().substring(1)));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUserBalance(double value){
        String uid = mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("uid", "");
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