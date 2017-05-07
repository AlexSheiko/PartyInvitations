package com.alexsheiko.party.util

import android.os.Bundle
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.ui.BaseActivity
import com.google.firebase.analytics.FirebaseAnalytics.Event.APP_OPEN
import com.google.firebase.analytics.FirebaseAnalytics.Event.VIEW_ITEM
import com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID

fun BaseActivity.logEventAppOpen() {
    if (!DEBUG) {
        mAnalytics.logEvent(APP_OPEN, Bundle())
    }
}

fun BaseActivity.logSwitchTemplate() {
    if (!DEBUG) {
        mAnalytics.logEvent(VIEW_ITEM, Bundle())
    }
}

fun BaseActivity.logShare(successful: Boolean) {
    if (!DEBUG) {
        val bundle = Bundle()
        bundle.putString(ITEM_ID, "$successful success")
        mAnalytics.logEvent(APP_OPEN, bundle)
    }
}