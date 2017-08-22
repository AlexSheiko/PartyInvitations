package com.alexsheiko.party.ui.pay

import android.content.Intent
import android.os.Bundle
import com.alexsheiko.party.BuildConfig.DEBUG
import com.alexsheiko.party.ui.base.BaseActivityWithBilling
import com.alexsheiko.party.util.*
import com.alexsheiko.party.util.billing.IabResult
import com.alexsheiko.party.util.billing.Purchase
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class PayActivity : BaseActivityWithBilling() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modeBoostReviews = remoteConfig
                .getBoolean("mode_boost_reviews")
        if (modeBoostReviews) {
            startActivityForResult(
                    intentFor<PayOrReviewDialog>(),
                    234)
        } else {
            mHelper.startSetup { result ->
                if (result.isSuccess) {
                    startPaying()
                } else {
                    longToast("Error: ${result.message}")
                    finish()
                }
            }
        }
    }

    private fun startPaying() {
        if (!DEBUG) {
            buy("1")
        } else {
            saveProAndExit()
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
        val listener: (IabResult, Purchase?) -> Unit = { result, _ ->
            if (result.isSuccess) {
                saveProAndExit()
            } else {
                toast("Error: ${result.message}")
                finish()
            }
        }
        mHelper.launchPurchaseFlow(this,
                productId, 10001, listener, "")
    }

    fun saveProAndExit() {
        saveUserPro()

        setResult(RESULT_OK)
        finish()
    }
}