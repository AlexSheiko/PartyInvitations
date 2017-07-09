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

    inputHeader.setTypeface(null, BOLD)
    inputHeader.setText(title, EDITABLE)
    inputBody.setText(address, EDITABLE)

    textHeaderPreview.text = title
    textBodyPreview.text = address

    showImage(image)
}