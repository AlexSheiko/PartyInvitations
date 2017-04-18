package com.alexsheiko.invite.util

import com.alexsheiko.invite.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

fun initCustomFont() {
    CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Brandon-Regular.otf")
            .setFontAttrId(R.attr.fontPath)
            .build())
}