package com.alexsheiko.party.util

import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.saveTextAndImage() {
    val title = inputTitle.text.toString()
    val details = inputText.text.toString()

    prefs.edit()
            .putString("address", details)
            .apply()
    prefs.edit()
            .putString("title", title)
            .apply()
    prefs.edit()
            .putInt("image", Integer.parseInt(imageView.tag.toString()))
            .apply()
}