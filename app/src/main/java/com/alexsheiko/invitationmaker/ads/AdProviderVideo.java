package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class AdProviderVideo implements RewardedVideoAdListener {

    private static final String ZONE_ID = "vz732ea85f536a4b0aae";
    private Activity mActivity;
    private AdColonyInterstitial mAdColony;
    private boolean mAdLoaded = false;
    private AdCloseListener mCloseListener;
    private Snackbar mSnackbar;
    private RewardedVideoAd mAd;

    public void prepare(Activity activity, AdCloseListener closeListener) {
        mActivity = activity;
        mCloseListener = closeListener;

        AdColony.configure(mActivity, "appd3fbafd399de4909ab", ZONE_ID);

        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mAd.setRewardedVideoAdListener(this);

        loadVideo();
    }

    public void loadVideo() {
        mAd.loadAd("ca-app-pub-3038649646029056/3650335528", new AdRequest.Builder().build());

        AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                /** Store and use this ad object to onClickShow your ad when appropriate */
                mAdColony = ad;
                if (mAdColony != null) {
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
            mAdColony.show();
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

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
}