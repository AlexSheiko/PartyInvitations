package com.alexsheiko.party.ui

import android.content.Intent
import android.os.Bundle
import com.alexsheiko.party.R
import com.alexsheiko.party.util.billing.IabResult
import com.alexsheiko.party.util.billing.Purchase
import com.alexsheiko.party.util.logShare
import kotlinx.android.synthetic.main.activity_pay_dialog.*
import org.jetbrains.anko.onClick

class PayDialog : BaseActivityWithBilling() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_dialog)

        setClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mHelper.handleActivityResult(requestCode, resultCode, data)
    }

    private fun setClickListeners() {
        buttonFree.onClick { submit("free") }
        button1.onClick { buy("1") }
        button2.onClick { buy("2") }
        button3.onClick { buy("3") }
    }

    private fun buy(productId: String) {
        val listener: (IabResult?, Purchase?) -> Unit = { result, _ ->
            if (result != null && result.isSuccess) {
                submit(productId)
            }
        }
        mHelper.launchPurchaseFlow(this,
                productId, 10001, listener, "")
    }

    override fun submit(productId: String) {
        setResult(RESULT_OK)
        finish()

        logShare(productId)
    }
}
