package com.alexsheiko.party.ui

import android.os.Bundle
import com.alexsheiko.party.util.billing.IabHelper

abstract class BaseActivityWithBilling : BaseActivity() {

    val base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApV3Rnmfb3KUSB4eDAxi2mJQ/pVzE4i7wGSAK/grvpumHS2AcwUtTUHWHIsekWqURZGkkfgV9WMDxKSHt1N6Vl44Rl8HAPpEmaXDzE43vcvrvRlpNgkHyrZAOxPBaRuXkAb3j4Gg1sN9+BxfoHoG/QLWl0TtPk/Yy+/gizfuz6vORiy4Q8bWfWnyqSC9pNU2Xnbxa494Bfo0TBQ1VwZzosxaorUM3XnWakrWHowJgU1D81SDt414JmlN8OKcex5uWDPWTJ4PkZmWDgAk+3WX0WnGCEYu8EfLGCWlULqkBvqTb7x+0D33IL9v4AXYnqjQFNfWDx6iAEn25DTOBySIsXwIDAQAB"
    lateinit var mHelper: IabHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHelper = IabHelper(this, base64EncodedPublicKey)
    }

    abstract fun submit(productId: String)

    public override fun onDestroy() {
        super.onDestroy()
        mHelper.dispose()
    }
}