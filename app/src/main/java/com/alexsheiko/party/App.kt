package com.alexsheiko.party

import android.app.Application
import com.alexsheiko.party.BuildConfig.DEBUG
import com.google.firebase.crash.FirebaseCrash.setCrashCollectionEnabled

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setCrashCollectionEnabled(!DEBUG)
    }
}