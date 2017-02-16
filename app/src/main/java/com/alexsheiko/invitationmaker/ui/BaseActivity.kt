package com.alexsheiko.invitationmaker.ui

import android.R
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.alexsheiko.invitationmaker.util.extensions.PrefsExtensions
import com.alexsheiko.invitationmaker.util.extensions.ShareExtensions
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity(),
        PrefsExtensions, ShareExtensions {

    /**
     * Use custom font in all screens.
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}