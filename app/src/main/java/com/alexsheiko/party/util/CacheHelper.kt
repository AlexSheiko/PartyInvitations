package com.alexsheiko.party.util

import android.graphics.Typeface
import android.widget.TextView
import com.alexsheiko.party.R
import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.showTextAndImage() {
    val title = prefs.getString("title",
            getString(R.string.title_default))
    val address = prefs.getString("address",
            getString(R.string.details_default))
    val image = prefs.getInt("image",
            R.drawable.wedding_template_7)

    inputTitle.setTypeface(null, Typeface.BOLD)
    inputTitle.setText(title, TextView.BufferType.EDITABLE)
    inputText.setText(address, TextView.BufferType.EDITABLE)

    titleView.text = title
    textView.text = address

    showImage(image)
}