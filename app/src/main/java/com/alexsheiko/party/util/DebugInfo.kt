package com.alexsheiko.party.util

import android.annotation.SuppressLint
import com.alexsheiko.party.App
import com.alexsheiko.party.BuildConfig.DEBUG
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash.setCrashCollectionEnabled

@SuppressLint("MissingPermission")
fun App.dontCollectDebugInfo() {
    FirebaseAnalytics
            .getInstance(this)
            .setAnalyticsCollectionEnabled(!DEBUG)
    setCrashCollectionEnabled(!DEBUG)
}