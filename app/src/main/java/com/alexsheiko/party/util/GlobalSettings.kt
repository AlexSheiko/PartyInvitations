package com.alexsheiko.party.util

import android.app.Activity
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

val RESULT_PAY = 1
val RESULT_REVIEWED = 2

val remoteConfig: FirebaseRemoteConfig
        by lazy { FirebaseRemoteConfig.getInstance() }

fun Activity.initRemoteSettings(callback: (Boolean) -> Unit) {
    setConnectionRules()
    fetchReviewSetting(callback)
}

private fun setConnectionRules() {
    val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(DEBUG)
            .build()
    remoteConfig.setConfigSettings(configSettings)
    remoteConfig.setDefaults(R.xml.remote_config_defaults)
}

fun Activity.fetchReviewSetting(callback: (Boolean) -> Unit) {
    var cacheExpiration: Long = 60 * 60 * 24 // 24 hours in seconds.
    // If the app is using developer mode, cacheExpiration is set to 0, so each fetch will
    // retrieve values from the service.
    if (remoteConfig.info.configSettings.isDeveloperModeEnabled) {
        cacheExpiration = 0
    }
    remoteConfig.fetch(cacheExpiration)
            .addOnCompleteListener(this, { task ->
                if (task.isSuccessful) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig.activateFetched()
                }
                val modeBoostReviews = remoteConfig.getBoolean("mode_boost_reviews")
                callback.invoke(modeBoostReviews)
            })
}