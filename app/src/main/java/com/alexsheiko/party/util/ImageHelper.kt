package com.alexsheiko.party.util

import com.alexsheiko.party.R
import com.alexsheiko.party.ui.MainActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.showImage(resId: Int) {
    Glide.with(this)
            .load(resId)
            .into(imageView)
    mSelectedTemplate = resId
}

val templates = listOf(
        R.drawable.party_template_1,
        R.drawable.party_template_2,
        R.drawable.party_template_5,
        R.drawable.party_template_8,
        R.drawable.wedding_template_2,
        R.drawable.wedding_template_3,
        R.drawable.wedding_template_4,
        R.drawable.wedding_template_6)

var templateIndex = 0

fun MainActivity.previousTemplate() {
    templateIndex--

    if (templateIndex < 0) {
        templateIndex = templates.size - 1
    }
    refreshTemplateView()
}

fun MainActivity.nextTemplate() {
    templateIndex++

    if (templateIndex > templates.size - 1) {
        templateIndex = 0
    }
    refreshTemplateView()
}

fun MainActivity.refreshTemplateView() {
    showImage(templates[templateIndex])

    logSwitchTemplate()
}
