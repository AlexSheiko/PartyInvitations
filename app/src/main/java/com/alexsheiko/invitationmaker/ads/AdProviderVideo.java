package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;

public class AdProviderVideo {

    private static final String ZONE_ID = "vz732ea85f536a4b0aae";
    private Activity mActivity;
    private AdColonyInterstitial mAd;
    private boolean mAdLoaded = false;
    private AdClosedListener mCloseListener;
    private Snackbar mSnackbar;

    public void prepare(Activity activity, AdClosedListener closeListener) {
        mActivity = activity;
        mCloseListener = closeListener;

        AdColony.configure(mActivity, "appd3fbafd399de4909ab", ZONE_ID);
        loadVideo();
    }

    public void loadVideo() {
        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                /** Store and use this ad object to onClickShow your ad when appropriate */
                mAd = ad;
                if (mAd != null) {
                    mAdLoaded = true;
                    if (mSnackbar != null && mSnackbar.isShown()) {
                        mSnackbar.dismiss();
                    }
                }
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                mCloseListener.onAdClosed();
            }
        };

        AdColony.requestInterstitial(ZONE_ID, listener);
    }

    public void onClickShow() {
        if (mAdLoaded) {
            mAd.show();
            Toast.makeText(mActivity, "Thank you, you help support the app!", Toast.LENGTH_LONG).show();
        } else {
            View parentLayout = mActivity.findViewById(android.R.id.content);
            mSnackbar = Snackbar.make(parentLayout, "Loading video...", Snackbar.LENGTH_INDEFINITE);
            mSnackbar.show();
        }
    }
}