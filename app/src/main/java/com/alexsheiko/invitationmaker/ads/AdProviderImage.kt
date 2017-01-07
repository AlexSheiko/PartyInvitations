package com.alexsheiko.invitationmaker.ads

import android.content.Context

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

class AdProviderImage {

    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardListener: RewardListener? = null

    fun prepare(context: Context, rewardListener: RewardListener) {
        mRewardListener = rewardListener

        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd!!.adUnitId = "ca-app-pub-3038649646029056/5392277129"

        requestNewInterstitial()
    }

    private fun requestNewInterstitial() {
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1A6B43A15E989B8B4F9121A9D649E323")
                .build()

        mInterstitialAd!!.loadAd(adRequest)
    }

    fun onClickShow() {
        if (mInterstitialAd!!.isLoaded) {
            mInterstitialAd!!.show()
        } else {
            mInterstitialAd!!.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    mInterstitialAd!!.show()
                    mInterstitialAd!!.adListener = null
                }

                override fun onAdClosed() {
                    mRewardListener!!.onRewarded()
                }
            }
        }
    }
}
