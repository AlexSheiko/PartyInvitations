package com.alexsheiko.invitationmaker;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

class AdProviderVideo implements RewardedVideoAdListener {

    private RewardedVideoAd mAd;
    private boolean mClickedShow = false;

    void prepare(Context context) {
        mAd = MobileAds.getRewardedVideoAdInstance(context);
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mAd.loadAd("",
                new AdRequest.Builder().build());
    }

    void show() {
        if (mAd.isLoaded()) {
            mAd.show();
        } else {
            mClickedShow = true; // wait for onRewardedVideoAdLoaded()
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mClickedShow) {
            mAd.show();
        }
        mClickedShow = false;
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
