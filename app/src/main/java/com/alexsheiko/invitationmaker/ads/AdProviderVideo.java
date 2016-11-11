package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;

public class AdProviderVideo {

    private static final String ZONE_ID = "vz732ea85f536a4b0aae";
    private Context mContext;
    private AdColonyInterstitial mAd;
    private boolean mAdLoaded = false;

    public void prepare(Context context) {
        mContext = context;

        AdColony.configure((Activity) mContext, "appd3fbafd399de4909ab", ZONE_ID);
        loadVideo();
    }

    private void loadVideo() {
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                /** Store and use this ad object to onClickShow your ad when appropriate */
                mAd = ad;
                if (mAd != null) {
                    mAdLoaded = true;
                }
            }
        };

        AdColony.requestInterstitial(ZONE_ID, listener);
    }

    public void onClickShow() {
        if (mAdLoaded) {
            mAd.show();
        } else {
            Toast.makeText(mContext, "Loading...", Toast.LENGTH_SHORT).show();
        }
    }
}