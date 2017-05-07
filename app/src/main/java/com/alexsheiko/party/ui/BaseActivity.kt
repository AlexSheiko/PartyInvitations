package com.alexsheiko.party.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alexsheiko.party.util.logEventAppOpen
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.getInstance
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap

abstract class BaseActivity : AppCompatActivity() {

    val mAnalytics: FirebaseAnalytics by lazy { getInstance(this) }

    override fun attachBaseContext(newBase: Context) {
        // Use custom font
        super.attachBaseContext(wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logEventAppOpen()
    }
}