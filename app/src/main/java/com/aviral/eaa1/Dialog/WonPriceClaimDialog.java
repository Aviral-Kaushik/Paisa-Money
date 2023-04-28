package com.aviral.eaa1.Dialog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aviral.eaa1.Fragments.ScratchCardFragment;
import com.aviral.eaa1.Fragments.SpinFragment;
import com.aviral.eaa1.Models.UserData;
import com.aviral.eaa1.R;
import com.aviral.eaa1.Utils.AdsParameters;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.Objects;

public class WonPriceClaimDialog extends DialogFragment implements IUnityAdsInitializationListener {

    private static final String TAG = "AviralAds";

    private View view;

    private String wonAmount;
    private String source;
    private Activity activity;
    private UserData userData;

    public WonPriceClaimDialog(String wonAmount,
                               String source,
                               Activity activity,
                               UserData userData) {
        this.wonAmount = wonAmount;
        this.source = source;
        this.activity = activity;
        this.userData = userData;
    }

    public WonPriceClaimDialog() {

    }

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {

            UnityAds.show(activity, AdsParameters.interstitialAndroidAdUnitId, new UnityAdsShowOptions(), showListener);

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

            view.setVisibility(View.GONE);

//            if (source.equals(requireContext().getString(R.string.spin_fragment))) {
//
//                Bundle userDataBundle = new Bundle();
//                userDataBundle.putParcelable(getString(R.string.user_data), userData);
//
//                SpinFragment spinFragment = new SpinFragment();
//                spinFragment.setArguments(userDataBundle);
//
//                FragmentTransaction fragmentTransaction1 = getParentFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.slide_in,
//                                R.anim.fade_out,
//                                R.anim.fade_in,
//                                R.anim.slide_out
//                        );
//                fragmentTransaction1.replace(R.id.main_container, spinFragment);
//                fragmentTransaction1.commitAllowingStateLoss();
//
//            } else if (source.equals(requireContext().getString(R.string.scratch_card))) {
//
//                Bundle userDataBundle = new Bundle();
//                userDataBundle.putParcelable(getString(R.string.user_data), userData);
//
//                ScratchCardFragment scratchCardFragment = new ScratchCardFragment();
//                scratchCardFragment.setArguments(userDataBundle);
//
//                FragmentTransaction fragmentTransaction = getParentFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.slide_in,
//                                R.anim.fade_out,
//                                R.anim.fade_in,
//                                R.anim.slide_out
//                        );
//                fragmentTransaction.replace(R.id.main_container, scratchCardFragment);
//                fragmentTransaction.commitAllowingStateLoss();
//
//            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_won_price_dialog, container, false);

        ConstraintLayout wonPriceClaimed = view.findViewById(R.id.wonPrice);

        UnityAds.initialize(requireActivity().getApplicationContext(),
                AdsParameters.unityGameID, AdsParameters.testMode, this);

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Objects.requireNonNull(getDialog()).getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;

        wonPriceClaimed.setOnClickListener(view1 -> DisplayInterstitialAd());

        TextView amount = view.findViewById(R.id.tv_amount);
//        amount.setText(roundOfNumber(wonAmount));
        amount.setText(wonAmount);

        return view;
    }

    @Override
    public void onInitializationComplete() {
        Log.d(TAG, "onInitializationComplete: Ads Initialization Complete");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.d(TAG, "onInitializationFailed: Ads Initialization failed " + message);
    }

    public void DisplayInterstitialAd() {
        UnityAds.load(AdsParameters.interstitialAndroidAdUnitId, loadListener);

        view.setVisibility(View.GONE);

//        if (source.equals(requireContext().getString(R.string.spin_fragment))) {
//
//            Bundle userDataBundle = new Bundle();
//            userDataBundle.putParcelable(getString(R.string.user_data), userData);
//
//            SpinFragment spinFragment = new SpinFragment();
//            spinFragment.setArguments(userDataBundle);
//
//            FragmentTransaction fragmentTransaction1 = getParentFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(
//                            R.anim.slide_in,
//                            R.anim.fade_out,
//                            R.anim.fade_in,
//                            R.anim.slide_out
//                    );
//            fragmentTransaction1.replace(R.id.main_container, spinFragment);
//            fragmentTransaction1.commitAllowingStateLoss();
//
//        } else if (source.equals(requireContext().getString(R.string.scratch_card))) {
//
//            Bundle userDataBundle = new Bundle();
//            userDataBundle.putParcelable(getString(R.string.user_data), userData);
//
//            ScratchCardFragment scratchCardFragment = new ScratchCardFragment();
//            scratchCardFragment.setArguments(userDataBundle);
//
//            FragmentTransaction fragmentTransaction = getParentFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(
//                            R.anim.slide_in,
//                            R.anim.fade_out,
//                            R.anim.fade_in,
//                            R.anim.slide_out
//                    );
//            fragmentTransaction.replace(R.id.main_container, scratchCardFragment);
//            fragmentTransaction.commitAllowingStateLoss();
//
//        }
    }
}
