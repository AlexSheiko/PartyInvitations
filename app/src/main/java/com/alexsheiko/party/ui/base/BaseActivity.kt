package com.alexsheiko.party.ui.base

import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.getInstance

abstract class BaseActivity : AppCompatActivity() {

    val mAnalytics: FirebaseAnalytics by lazy { getInstance(this) }
}