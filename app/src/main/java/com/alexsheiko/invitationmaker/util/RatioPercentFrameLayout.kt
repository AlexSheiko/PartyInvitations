package com.alexsheiko.invitationmaker.util

import android.content.Context
import android.support.percent.PercentFrameLayout
import android.util.AttributeSet

class RatioPercentFrameLayout(context: Context?, attrs: AttributeSet?)
    : PercentFrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec * 4 / 3)
    }
}
