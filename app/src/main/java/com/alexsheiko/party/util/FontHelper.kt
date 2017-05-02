package com.alexsheiko.party.util

import com.alexsheiko.party.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

fun initCustomFont() {
    CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Brandon-Regular.otf")
            .setFontAttrId(R.attr.fontPath)
            .build())
}