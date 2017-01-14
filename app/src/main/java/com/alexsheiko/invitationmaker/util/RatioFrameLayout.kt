package com.alexsheiko.invitationmaker.util

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.widget.ImageView

class RatioImageView(context: Context, attrs: AttributeSet?) :
        ImageView(context, attrs) {

    private val WIDTH_RATIO = 4
    private val HEIGHT_RATIO = 4

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec)
        val heightSize = HEIGHT_RATIO / WIDTH_RATIO * widthSize
        val newHeightSpec = makeMeasureSpec(heightSize, EXACTLY)
        super.onMeasure(newHeightSpec, widthMeasureSpec)
    }
}
