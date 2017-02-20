package com.alexsheiko.invitationmaker.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.alexsheiko.invitationmaker.R.id.home
import com.alexsheiko.invitationmaker.util.extensions.BaseExtensions
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper.wrap

abstract class BaseActivity : AppCompatActivity(),
        BaseExtensions {

    override fun attachBaseContext(newBase: Context) {
        // Use custom font
        super.attachBaseContext(wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}