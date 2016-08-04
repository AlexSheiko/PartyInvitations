package com.alexsheiko.invitationmaker;

import android.app.Application;

import com.adobe.creativesdk.aviary.IAviaryClientCredentials;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class InvitationApplication extends Application implements IAviaryClientCredentials {

    private static final String CREATIVE_SDK_CLIENT_ID = "56d4547128e84e079f6e1ef131607153";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "a46a3b39-4a9e-42c2-a5bc-6d7487e21943";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String getBillingKey() {
        return "";
    }
}
