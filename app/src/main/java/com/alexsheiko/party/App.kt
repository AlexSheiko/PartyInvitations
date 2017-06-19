package com.alexsheiko.party

import android.app.Application
import com.alexsheiko.party.BuildConfig.DEBUG
import com.google.firebase.crash.FirebaseCrash

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseCrash.setCrashCollectionEnabled(!DEBUG)
    }
}