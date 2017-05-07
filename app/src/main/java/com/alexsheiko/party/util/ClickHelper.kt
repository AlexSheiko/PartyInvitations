package com.alexsheiko.party.util

import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick

fun MainActivity.setClickListeners() {
    shareButton.onClick { captureCanvas() }
    imageView.onClick { changeTemplate() }
}