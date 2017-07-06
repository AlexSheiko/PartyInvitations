package com.alexsheiko.party.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import com.alexsheiko.party.R
import com.alexsheiko.party.util.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var mSelectedTemplate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showTextAndImage()
        setClickListeners()
        reactToInput()
    }

    override fun onResume() {
        super.onResume()
        if (isUserPro() || MODE_BOOST_REVIEWS) {
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
            shareImage(getImageUri())
        }
    }
}