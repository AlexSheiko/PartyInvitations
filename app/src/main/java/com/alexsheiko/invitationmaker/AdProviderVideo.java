package com.alexsheiko.invitationmaker;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

class AdProviderVideo {

    private RewardedVideoAd mAd;

    void prepare(Context context) {
        mAd = MobileAds.getRewardedVideoAdInstance(context);
        mAd.setRewardedVideoAdListener(null);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mAd.loadAd("",
                new AdRequest.Builder().build());
    }

    void show() {

    }
}
