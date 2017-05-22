package com.alexsheiko.party.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexsheiko.party.util.billing.IabResult
import com.alexsheiko.party.util.billing.Purchase
import com.alexsheiko.party.util.logPurchaseCompleted
import com.alexsheiko.party.util.logPurchaseStarted

class PayDialog : BaseActivityWithBilling() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHelper.startSetup { result ->
            if (result.isSuccess) {
                buy("3")
            } else {
                Log.d("TAG", "Problem setting up In-app Billing: " + result)
                submit("free")
            }
        }
        logPurchaseStarted()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mHelper.handleActivityResult(requestCode, resultCode, data)
    }

    private fun buy(productId: String) {
        val listener: (IabResult?, Purchase?) -> Unit = { result, _ ->
            if (result != null && result.isSuccess) {
                submit(productId)
            } else {
                finish()
            }
        }
        mHelper.launchPurchaseFlow(this,
                productId, 10001, listener, "")
    }

    override fun submit(productId: String) {
        setResult(RESULT_OK)
        finish()

        logPurchaseCompleted()
    }
}
