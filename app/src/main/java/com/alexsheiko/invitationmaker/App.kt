package com.alexsheiko.invitationmaker

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init default font
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Brandon-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}