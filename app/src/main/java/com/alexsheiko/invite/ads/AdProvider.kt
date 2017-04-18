package com.alexsheiko.invite.ads

import android.content.Context

class AdProvider(mContext: Context) {

    private var mInterstitialAd: InterstitialAd = InterstitialAd(mContext)

    init {
        mInterstitialAd.adUnitId = "ca-app-pub-3038649646029056/5392277129"
    }

    fun loadInBackground() {
            requestNewInterstitial()
    }

    private fun requestNewInterstitial() {
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1A6B43A15E989B8B4F9121A9D649E323")
                .build()
        mInterstitialAd.loadAd(adRequest)
    }

    fun show() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    mInterstitialAd.show()
                    mInterstitialAd.adListener = null
                }
            }
        }
    }
}
