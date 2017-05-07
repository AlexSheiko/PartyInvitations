package com.alexsheiko.party.ui

import android.os.Bundle
import com.alexsheiko.party.R
import com.alexsheiko.party.util.reactToInput
import com.alexsheiko.party.util.saveTextAndImage
import com.alexsheiko.party.util.setClickListeners
import com.alexsheiko.party.util.showTextAndImage

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showTextAndImage()
        setClickListeners()
        reactToInput()
    }

    override fun onStop() {
        super.onStop()
        saveTextAndImage()
    }
}