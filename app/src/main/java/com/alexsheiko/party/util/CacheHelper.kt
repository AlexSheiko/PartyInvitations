package com.alexsheiko.party.util

import android.graphics.Typeface.BOLD
import android.widget.TextView.BufferType.EDITABLE
import com.alexsheiko.party.R
import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.showTextAndImage() {
    val title = prefs.getString("title",
            getString(R.string.title_default))
    val address = prefs.getString("address",
            getString(R.string.details_default))
    val image = prefs.getInt("image",
            R.drawable.party_template_5)

    inputTitle.setTypeface(null, BOLD)
    inputTitle.setText(title, EDITABLE)
    inputText.setText(address, EDITABLE)

    titleView.text = title
    textView.text = address

    showImage(image)
}