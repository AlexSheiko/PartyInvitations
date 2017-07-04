package com.alexsheiko.party.util

import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.setClickListeners() {
    shareButton.setOnClickListener { captureCanvas() }
    imageView.setOnClickListener { refreshTemplateView() }
    buttonPrevious.setOnClickListener { previousTemplate() }
    buttonNext.setOnClickListener { nextTemplate() }
}