package com.alexsheiko.invitationmaker.ads;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdProviderImage {

    private InterstitialAd mInterstitialAd;
    private Context mContext;

    public void prepare(Context context) {
        mContext = context;
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

    public void show() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(mContext, "Loading ad...", Toast.LENGTH_SHORT).show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(null);
                }
            });
        }
    }
}
