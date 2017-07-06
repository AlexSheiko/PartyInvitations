package com.alexsheiko.party.util

import android.os.Bundle
import com.alexsheiko.party.ui.base.BaseActivity
import com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT

fun BaseActivity.logPurchaseStarted() {
    mAnalytics.logEvent(BEGIN_CHECKOUT, Bundle())
}