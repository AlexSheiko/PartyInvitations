package com.alexsheiko.party.ui.pay

import android.os.Bundle
import com.alexsheiko.party.R
import com.alexsheiko.party.ui.base.BaseActivity
import com.alexsheiko.party.util.RESULT_PAY
import com.alexsheiko.party.util.RESULT_REVIEWED
import com.alexsheiko.party.util.saveUserPro
import kotlinx.android.synthetic.main.dialog_pay_or_review.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast

class PayOrReviewDialog : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_or_review)

        setClickListeners()
    }

    private fun setClickListeners() {
        buttonPay.setOnClickListener {
            setResult(RESULT_PAY)
            finish()
        }
        buttonReview.setOnClickListener {
            browse("https://play.google.com/store/apps/details?id=$packageName")
            toast("Scroll down to see review section")
            saveUserPro()

            setResult(RESULT_REVIEWED)
            finish()
        }
    }
}
