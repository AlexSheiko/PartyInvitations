package com.alexsheiko.party.util

import android.os.Bundle
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.ui.BaseActivity
import com.google.firebase.analytics.FirebaseAnalytics.Event.*

fun BaseActivity.logSwitchTemplate() {
    if (!DEBUG) {
        mAnalytics.logEvent(VIEW_ITEM, Bundle())
    }
}

fun BaseActivity.logPurchaseStarted() {
    if (!DEBUG) {
        mAnalytics.logEvent(BEGIN_CHECKOUT, Bundle())
    }
}

fun BaseActivity.logPurchaseCompleted() {
    if (!DEBUG) {
        mAnalytics.logEvent(ECOMMERCE_PURCHASE, Bundle())
    }
}