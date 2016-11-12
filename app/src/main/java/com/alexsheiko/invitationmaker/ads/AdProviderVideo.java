package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;

public class AdProviderVideo {

    private static final String ZONE_ID = "vz732ea85f536a4b0aae";
    private Activity mActivity;
    private AdColonyInterstitial mAd;
    private boolean mAdLoaded = false;
    private AdCloseListener mCloseListener;
    private Snackbar mSnackbar;

    public void prepare(Activity activity, AdCloseListener closeListener) {
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
                    dismissSnackbar();
                }
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                mCloseListener.onAdClosed();
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
                Toast.makeText(mActivity, "Thank you, you help support the app!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                super.onRequestNotFilled(zone);
                dismissSnackbar();
                mAdLoaded = false;
            }
        };

        AdColony.requestInterstitial(ZONE_ID, listener);
    }

    public void onClickShow() {
        if (mAdLoaded) {
            mAd.show();
        } else {
            loadVideo();
            showSnackBar();
        }
    }

    private void showSnackBar() {
        View parentLayout = mActivity.findViewById(android.R.id.content);
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Loading video...", Snackbar.LENGTH_INDEFINITE);
        }
        mSnackbar.show();
    }

    private void dismissSnackbar() {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }
}