package com.alexsheiko.party.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Use custom font
        super.attachBaseContext(wrap(newBase))
    }
}