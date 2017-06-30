package com.alexsheiko.party.ui

import android.os.Bundle
import com.alexsheiko.party.R
import com.alexsheiko.party.util.saveUserPro
import kotlinx.android.synthetic.main.dialog_pay_options.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast

val RESULT_PAY = 1
val RESULT_REVIEWED = 2

class PayOptionsDialog : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_options)

        setClickListeners()
    }

    private fun setClickListeners() {
        button1.onClick {
            setResult(RESULT_PAY)
            finish()
        }
        buttonFree.onClick {
            browse("https://play.google.com/store/apps/details?id=$packageName")
            toast("Scroll down to see review section")
            saveUserPro()

            setResult(RESULT_REVIEWED)
            finish()
        }
    }
}
