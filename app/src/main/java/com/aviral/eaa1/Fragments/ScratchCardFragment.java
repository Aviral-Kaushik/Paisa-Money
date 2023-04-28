package com.aviral.eaa1.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentScratchCardBinding;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.Random;

public class ScratchCardFragment extends Fragment implements IUnityAdsInitializationListener {

    private static final String TAG = "AviralAds";

    private FragmentScratchCardBinding binding;

    private UserData userData;

    private int chancesLeft;

    private LoadingDialog loadingDialog;

    private ApiBackendProvider backendProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScratchCardBinding.inflate(inflater, container, false);

        UnityAds.initialize(requireActivity().getApplicationContext(),
                AdsParameters.unityGameID, AdsParameters.testMode, this);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        backendProvider = new ApiBackendProvider(requireContext());

        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();

        binding.amountWon.setText(String.format("₹%s", generateRandomNumber()));

        binding.btnBalance.setText(String.format("₹%s", userData.getBalance()));

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
                        Double.parseDouble(userData.getBalance())
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

        return view;
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
        loadingDialog.show();

        fetchUserData();
    }

    private void fetchUserData() {

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());

        userData = backendProvider.fetchUserData(userData.getEmail());

        loadingDialog.dismiss();

    }

    private void updateUserBalance(String earnedAmount) {

//        loadingDialog.show();

        backendProvider.updateUserBalance(
                userData.getUid(),
                String.valueOf(earnedAmount)
        );

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();

            Bundle userDataBundle = new Bundle();
            userDataBundle.putParcelable(getString(R.string.user_data), userData);

            ScratchCardFragment scratchCardFragment = new ScratchCardFragment();
            scratchCardFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction = getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction.attach(scratchCardFragment);
            fragmentTransaction.detach(scratchCardFragment);
            fragmentTransaction.attach(scratchCardFragment);
            fragmentTransaction.replace(R.id.main_container, scratchCardFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }, 2000);

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

    public void DisplayRewardedAd() {
        UnityAds.load(AdsParameters.rewardedAndroidAdUnitId, loadListener);

        decrementChances();

        WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
                binding.amountWon.getText().toString(),
                getString(R.string.scratch_fragment),
                requireActivity(),
                userData);

        updateUserBalance(binding.amountWon.getText().toString().substring(1));

        wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}