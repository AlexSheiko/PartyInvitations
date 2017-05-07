package com.alexsheiko.party.util

import android.os.Bundle
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.ui.BaseActivity
import com.google.firebase.analytics.FirebaseAnalytics.Event.APP_OPEN

fun BaseActivity.logEventAppOpen() {
    if (!DEBUG) {
        mAnalytics.logEvent(APP_OPEN, Bundle())
    }
}