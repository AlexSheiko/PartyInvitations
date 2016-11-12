package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.chartboost.sdk.Chartboost;
import com.google.ads.mediation.chartboost.ChartboostAdapter;
import com.google.ads.mediation.unity.UnityAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;
import com.unity3d.ads.UnityAds;

public class AdProviderVideo implements RewardedVideoAdListener {

    private Activity mActivity;
    private boolean mAdLoaded = false;
    private AdCloseListener mCloseListener;
    private Snackbar mSnackbar;
    private RewardedVideoAd mAd;

    public void prepare(Activity activity, AdCloseListener closeListener) {
        mActivity = activity;
        mCloseListener = closeListener;

        initProviders();

        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mAd.setRewardedVideoAdListener(this);

        loadVideo();
    }

    private void initProviders() {
        AdColony.configure(mActivity, "appd3fbafd399de4909ab", "vz732ea85f536a4b0aae");
        Chartboost.startWithAppId(mActivity, "57c4638143150f2dbda90642", "97d51d16f7428263e14b26881a665e87b23f47ee");
        Chartboost.onCreate(mActivity);
        UnityAds.initialize(mActivity, "1127394", null);
    }

    public void loadVideo() {
        AdColonyBundleBuilder.setZoneId("vz732ea85f536a4b0aae");

        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build())
                .addNetworkExtrasBundle(ChartboostAdapter.class, Bundle.EMPTY)
                .addNetworkExtrasBundle(UnityAdapter.class, Bundle.EMPTY)
                .build();

        mAd.loadAd("ca-app-pub-3038649646029056/3650335528", adRequest);
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

    @Override
    public void onRewardedVideoAdLoaded() {
        mAdLoaded = true;
        dismissSnackbar();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(mActivity, "Thank you, you help support the app!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoAdClosed() {
        mCloseListener.onAdClosed();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        dismissSnackbar();
        mAdLoaded = false;
    }

    public RewardedVideoAd getAd() {
        return mAd;
    }
}