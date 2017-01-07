package com.alexsheiko.invitationmaker

import android.support.multidex.MultiDexApplication

import com.adobe.creativesdk.aviary.IAviaryClientCredentials
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation

import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class InvitationApp : MultiDexApplication(), IAviaryClientCredentials {

    override fun onCreate() {
        super.onCreate()
        AdobeCSDKFoundation.initializeCSDKFoundation(applicationContext)

        // Init default font
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Brandon-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    override fun getClientID(): String {
        return CREATIVE_SDK_CLIENT_ID
    }

    override fun getClientSecret(): String {
        return CREATIVE_SDK_CLIENT_SECRET
    }

    override fun getBillingKey(): String {
        return ""
    }

    companion object {

        private val CREATIVE_SDK_CLIENT_ID = "56d4547128e84e079f6e1ef131607153"
        private val CREATIVE_SDK_CLIENT_SECRET = "a46a3b39-4a9e-42c2-a5bc-6d7487e21943"
    }
}
