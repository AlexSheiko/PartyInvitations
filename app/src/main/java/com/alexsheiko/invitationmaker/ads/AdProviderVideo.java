package com.alexsheiko.invitationmaker.ads;

import android.app.Activity;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.chartboost.sdk.Chartboost;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;
import com.unity3d.ads.UnityAds;

import static com.unity3d.ads.properties.ClientProperties.getApplicationContext;

public class AdProviderVideo implements RewardedVideoAdListener {

    private Activity mActivity;
    private boolean mAdLoaded = false;
    private boolean mShowOnLoad = false;
    private RewardListener mRewardListener;
    private Snackbar mSnackbar;
    private RewardedVideoAd mAd;

    public void prepare(Activity activity, RewardListener rewardListener) {
        mActivity = activity;
        mRewardListener = rewardListener;

        initProviders();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3038649646029056~9696869128");
        // Use an activity context to get the rewarded video instance.
        mAd = MobileAds.getRewardedVideoAdInstance(mActivity);
        mAd.setRewardedVideoAdListener(this);

        loadVideo();
    }

    private void initProviders() {
        // TODO: Make class a singeleton to only initialize once
        AdColony.configure(mActivity, "version:3.0,store:google", "appd3fbafd399de4909ab", "vz732ea85f536a4b0aae");
        Chartboost.startWithAppId(mActivity, "57c4638143150f2dbda90642", "97d51d16f7428263e14b26881a665e87b23f47ee");
        UnityAds.initialize(mActivity, "1127394", null);
    }

    public void loadVideo() {
        AdColonyBundleBuilder.setZoneId("vz732ea85f536a4b0aae");

        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build())
                //                .addNetworkExtrasBundle(ChartboostAdapter.class, new ChartboostAdapter.ChartboostExtrasBundleBuilder().build())
                //                .addNetworkExtrasBundle(UnityAdapter.class, Bundle.EMPTY)
                .build();

        mAd.loadAd("ca-app-pub-3038649646029056/3650335528", adRequest);
    }

    public void onClickShow() {
        if (mAdLoaded) {
            mAd.show();
        } else {
            loadVideo();
            showSnackBar();
            mShowOnLoad = true;
        }
    }

    private void showSnackBar() {
        View parentLayout = mActivity.findViewById(android.R.id.content);
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Loading video... Thank you for waiting", Snackbar.LENGTH_INDEFINITE);
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

        if (mShowOnLoad) {
            onClickShow();
            mShowOnLoad = false;
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(mActivity, "Please continue, you help support the app!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(mActivity, "Thank you for being patient!", Toast.LENGTH_LONG).show();
        Toast.makeText(mActivity, "Thank you for being patient!", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(() ->
                        Toast.makeText(mActivity, "You're awesome!'",
                                Toast.LENGTH_LONG).show(),
                10000);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardListener.onRewarded();
        Toast.makeText(mActivity, "Enjoy using the template!", Toast.LENGTH_LONG).show();
        reset();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        reset();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        dismissSnackbar();
        reset();

        View parentLayout = mActivity.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, "No ads available, try templates without green label", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void reset() {
        mAd.setRewardedVideoAdListener(null);
        mAdLoaded = false;
        loadVideo();
    }

    public RewardedVideoAd getAd() {
        return mAd;
    }
}