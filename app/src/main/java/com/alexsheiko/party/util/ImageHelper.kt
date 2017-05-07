package com.alexsheiko.party.util

import com.alexsheiko.party.R
import com.alexsheiko.party.ui.MainActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

fun MainActivity.showImage(resId: Int) {
    Glide.with(this)
            .load(resId)
            .into(imageView)
    mSelectedTemplate = resId
}

fun MainActivity.changeTemplate() {
    val images = listOf(
            R.drawable.party_template_1,
            R.drawable.party_template_2,
            R.drawable.party_template_5,
            R.drawable.party_template_8,
            R.drawable.wedding_template_2,
            R.drawable.wedding_template_3,
            R.drawable.wedding_template_4,
            R.drawable.wedding_template_6)

    Collections.shuffle(images)

    showImage(images[0])

    logSwitchTemplate()
}
