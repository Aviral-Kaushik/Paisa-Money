package com.aviral.eaa1.Fragments;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aviral.eaa1.Dialog.WonPriceClaimDialog;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.aviral.eaa1.Utils.ApiBackendProvider;
import com.aviral.eaa1.Utils.LoadingDialog;
import com.aviral.eaa1.databinding.FragmentSpinBinding;
import com.google.android.material.snackbar.Snackbar;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.Random;

public class SpinFragment extends Fragment implements IUnityAdsInitializationListener {

    private static final String TAG = "AviralAds";

    private FragmentSpinBinding binding;

    final double[] sectors = {0.20, 0.75, 0.05, 0.25, 0.15, 1.00, 0.10, 0.50};
    final int[] sectorsDegree = new int[sectors.length];

    int randomSectorIndex = 0;
    boolean spinning = false;

    Random random = new Random();

    private UserData userData;

    private int chancesLeft;

    private LoadingDialog loadingDialog;

    private ApiBackendProvider backendProvider;

    private double earnedAmount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSpinBinding.inflate(inflater, container, false);

        userData = requireArguments().getParcelable(requireContext().getString(R.string.user_data));

        UnityAds.initialize(requireActivity().getApplicationContext(),
                AdsParameters.unityGameID, AdsParameters.testMode, this);

        backendProvider = new ApiBackendProvider(requireContext());

        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();

        binding.btnBalance.setText(String.format("₹%s", userData.getBalance()));

        getChances();

        binding.btnSpin.setOnSlideCompleteListener(slideToActView -> startSpin());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadingDialog.show();

        fetchUserData();

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
//                spinning = false;
//
//                decrementChances();
//
//                WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
//                        "₹" + earnedAmount,
//                        getString(R.string.spin_fragment),
//                        requireActivity());
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

    private void fetchUserData() {

        ApiBackendProvider backendProvider = new ApiBackendProvider(requireContext());

        userData = backendProvider.fetchUserData(userData.getEmail());

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


                binding.btnBalance.setText(String.format("₹%s",
                        Double.parseDouble(userData.getBalance())
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

    private void updateUserBalance(double earnedAmount) {

//        loadingDialog.show();

        backendProvider.updateUserBalance(
                userData.getUid(),
                String.valueOf(earnedAmount)
        );

        new Handler().postDelayed(() -> {
            loadingDialog.dismiss();

            Bundle userDataBundle = new Bundle();
            userDataBundle.putParcelable(getString(R.string.user_data), userData);

            SpinFragment spinFragment = new SpinFragment();
            spinFragment.setArguments(userDataBundle);
            FragmentTransaction fragmentTransaction1 = getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    );
            fragmentTransaction1.attach(spinFragment);
            fragmentTransaction1.detach(spinFragment);
            fragmentTransaction1.attach(spinFragment);
            fragmentTransaction1.replace(R.id.main_container, spinFragment);
            fragmentTransaction1.commitAllowingStateLoss();
        }, 2000);

    }


    private void decrementChances() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("spinChances", Context.MODE_PRIVATE);
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


        new Handler().postDelayed(() -> {

            spinning = false;

            decrementChances();

            WonPriceClaimDialog wonPriceClaimDialog = new WonPriceClaimDialog(
                    "₹" + earnedAmount,
                    getString(R.string.spin_fragment),
                    requireActivity(),
                    userData);

            updateUserBalance(earnedAmount);

            wonPriceClaimDialog.show(getParentFragmentManager(), "Earned Amount");
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}