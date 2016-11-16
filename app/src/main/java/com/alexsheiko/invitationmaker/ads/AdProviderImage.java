package com.alexsheiko.invitationmaker.ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdProviderImage {

    private InterstitialAd mInterstitialAd;
    private Context mContext;
    private RewardListener mRewardListener;

    public void prepare(Context context, RewardListener rewardListener) {
        mContext = context;
        mRewardListener = rewardListener;
        // Initialize the Mobile Ads SDK.
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3038649646029056/5392277129");

        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1A6B43A15E989B8B4F9121A9D649E323")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void onClickShow() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(null);
                }

                @Override
                public void onAdClosed() {
                    mRewardListener.onRewarded();
                }
            });
        }
    }
}
