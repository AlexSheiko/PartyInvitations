package com.alexsheiko.invitationmaker.ads

import android.app.Activity
import android.os.Handler
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.chartboost.sdk.Chartboost
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.jirbo.adcolony.AdColony
import com.jirbo.adcolony.AdColonyAdapter
import com.jirbo.adcolony.AdColonyBundleBuilder
import com.unity3d.ads.UnityAds
import com.unity3d.ads.properties.ClientProperties.getApplicationContext
import org.jetbrains.anko.doAsync

class AdProviderVideo : RewardedVideoAdListener {

    private var mActivity: Activity? = null
    private var mAdLoaded = false
    private var mShowOnLoad = false
    private var mRewardListener: RewardListener? = null
    private var mSnackbar: Snackbar? = null
    var ad: RewardedVideoAd? = null
        private set
    private var mWatchingAd: Boolean = false
    private var mImageProvider: AdProviderImage? = null

    fun prepare(activity: Activity, rewardListener: RewardListener) {
        mActivity = activity
        mRewardListener = rewardListener

        doAsync { initProviders() }

        // Use an activity context to get the rewarded video instance.
        ad = MobileAds.getRewardedVideoAdInstance(mActivity)
        ad?.rewardedVideoAdListener = this

        loadVideo()
    }

    private fun initProviders() {
        // TODO: Make class a singeleton to only initialize once
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3038649646029056~9696869128")
        AdColony.configure(mActivity, "version:3.0,store:google", "appd3fbafd399de4909ab", "vz732ea85f536a4b0aae")
        Chartboost.startWithAppId(mActivity, "57c4638143150f2dbda90642", "97d51d16f7428263e14b26881a665e87b23f47ee")
        UnityAds.initialize(mActivity, "1127394", null)

        mImageProvider = AdProviderImage()
        mImageProvider!!.prepare(mActivity, mRewardListener)
    }

    private fun loadVideo() {
        AdColonyBundleBuilder.setZoneId("vz732ea85f536a4b0aae")

        val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdColonyAdapter::class.java, AdColonyBundleBuilder.build())
                .build()

        ad!!.loadAd("ca-app-pub-3038649646029056/3650335528", adRequest)
    }

    fun onClickShow() {
        if (mAdLoaded) {
            ad!!.show()
            mWatchingAd = true
        } else {
            loadVideo()
            showSnackBar()
            mShowOnLoad = true
        }
    }

    fun showEditorIfNeeded() {
        if (mWatchingAd) {
            mRewardListener!!.onRewarded()
            mWatchingAd = false
        }
    }

    private fun showSnackBar() {
        val parentLayout = mActivity!!.findViewById(android.R.id.content)
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Loading video... thank you for waiting  ☺", Snackbar.LENGTH_INDEFINITE)
        }
        mSnackbar!!.show()
    }

    private fun dismissSnackbar() {
        if (mSnackbar != null && mSnackbar!!.isShown) {
            mSnackbar!!.dismiss()
        }
    }

    override fun onRewardedVideoAdLoaded() {
        mAdLoaded = true
        dismissSnackbar()

        if (mShowOnLoad) {
            onClickShow()
            mShowOnLoad = false
        }
    }

    override fun onRewardedVideoAdOpened() {
        Toast.makeText(mActivity, "Please continue, you help support the app!", Toast.LENGTH_LONG).show()
    }

    override fun onRewardedVideoStarted() {
        Handler().postDelayed({
            Toast.makeText(mActivity, "You're awesome!",
                    Toast.LENGTH_LONG).show()
        },
                10000)

        Handler().postDelayed({
            Toast.makeText(mActivity, "Click the offer if you like it!",
                    Toast.LENGTH_LONG).show()
        },
                15000)

        Handler().postDelayed({
            Toast.makeText(mActivity, "Your progress is saved!",
                    Toast.LENGTH_LONG).show()
        },
                20000)
    }

    override fun onRewardedVideoAdClosed() {
    }

    override fun onRewarded(rewardItem: RewardItem) {
    }

    override fun onRewardedVideoAdLeftApplication() {
    }

    override fun onRewardedVideoAdFailedToLoad(i: Int) {
        dismissSnackbar()
        reset()

        mImageProvider!!.onClickShow()
    }

    private fun reset() {
        ad!!.rewardedVideoAdListener = null
        mAdLoaded = false
        loadVideo()
    }
}