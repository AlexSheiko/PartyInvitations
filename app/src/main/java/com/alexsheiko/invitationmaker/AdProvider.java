package com.alexsheiko.invitationmaker;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdProvider {

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
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void showImage() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(mContext, "Retry in 2 seconds", Toast.LENGTH_SHORT).show();
        }
    }
}
