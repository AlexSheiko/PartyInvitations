package com.alexsheiko.party.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.getInstance
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap

abstract class BaseActivity : AppCompatActivity() {

    val mAnalytics: FirebaseAnalytics by lazy { getInstance(this) }

    override fun attachBaseContext(newBase: Context) {
        // Use custom font
        super.attachBaseContext(wrap(newBase))
    }
}