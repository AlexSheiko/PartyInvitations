package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;

public class AdProviderVideo implements RewardedVideoAdListener {

    private Activity mActivity;
    private boolean mAdLoaded = false;
    private AdCloseListener mCloseListener;
    private Snackbar mSnackbar;
    private RewardedVideoAd mAd;

    public void prepare(Activity activity, AdCloseListener closeListener) {
        mActivity = activity;
        mCloseListener = closeListener;

        AdColony.configure(mActivity, "appd3fbafd399de4909ab", "vz732ea85f536a4b0aae");

        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mAd.setRewardedVideoAdListener(this);

        loadVideo();
    }

    public void loadVideo() {
        AdColonyBundleBuilder.setZoneId("vz732ea85f536a4b0aae");

        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build())
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
        Log.d("AdProviderVideo", "onRewardedVideoAdFailedToLoad with code " + i);
    }
}