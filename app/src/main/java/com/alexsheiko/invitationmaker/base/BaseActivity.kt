package com.alexsheiko.invitationmaker.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.alexsheiko.invitationmaker.util.Extensions
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity(), Extensions {

    /**
     * Use custom font in all screens.
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}