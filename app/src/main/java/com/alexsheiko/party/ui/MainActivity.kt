package com.alexsheiko.party.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import com.alexsheiko.party.R
import com.alexsheiko.party.util.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivityWithBilling() {

    var mSelectedTemplate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showTextAndImage()
        setClickListeners()
        reactToInput()
    }

    override fun onStart() {
        super.onStart()
        if (isUserPro()) {
            textPrice.visibility = GONE
        }
    }

    override fun onStop() {
        super.onStop()
        saveTextAndImage()
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            toast("Opening...")
            shareImage(getImageUri())
        }
    }
}