package com.alexsheiko.party.ui

import android.content.Intent
import android.os.Bundle
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.util.billing.IabResult
import com.alexsheiko.party.util.billing.Purchase
import com.alexsheiko.party.util.logPurchaseCompleted
import com.alexsheiko.party.util.logPurchaseStarted
import com.alexsheiko.party.util.saveUserPro
import org.jetbrains.anko.intentFor

class PayActivity : BaseActivityWithBilling() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivityForResult(intentFor<PayOptionsDialog>(), 234)
    }

    private fun startPaying() {
        if (!DEBUG) {
            buy("1")
        } else {
            submit()
        }
        logPurchaseStarted()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mHelper.handleActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_PAY) {
            startPaying()
        } else if (resultCode == RESULT_REVIEWED) {
            finish()
        }
    }

    private fun buy(productId: String) {
        val listener: (IabResult?, Purchase?) -> Unit = { result, _ ->
            if (result != null && result.isSuccess) {
                submit()
            } else {
                finish()
            }
        }
        mHelper.launchPurchaseFlow(this,
                productId, 10001, listener, "")
    }

    fun submit() {
        saveUserPro()
        logPurchaseCompleted()

        setResult(RESULT_OK)
        finish()
    }
}
