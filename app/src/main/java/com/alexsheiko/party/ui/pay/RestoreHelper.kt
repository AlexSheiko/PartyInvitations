package com.alexsheiko.party.ui.pay

import com.alexsheiko.party.ui.MainActivity
import com.alexsheiko.party.util.remoteConfig
import com.alexsheiko.party.util.saveUserPro
import org.jetbrains.anko.longToast

fun MainActivity.restorePurchaseIfNeeded() {
    val modeBoostReviews = remoteConfig
            .getBoolean("mode_boost_reviews")
    if (!modeBoostReviews) {
        mHelper.startSetup { result ->
            if (result.isSuccess) {
                mHelper.queryInventoryAsync(
                        false,
                        listOf("pro"),
                        emptyList(), { result2, inv ->
                    if (result2.isSuccess) {
                        if (inv.hasPurchase("pro")) {
                            saveUserPro()
                        }
                    } else {
                        longToast("Failed to query inventory: ${result2.message}")
                    }
                })
            } else {
                longToast("Failed to set up billing: ${result.message}")
                finish()
            }
        }
    }
}