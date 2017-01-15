package com.alexsheiko.invitationmaker.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class RatioImageView(context: Context, attrs: AttributeSet?) :
        ImageView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec * 4 / 3)
    }
}
