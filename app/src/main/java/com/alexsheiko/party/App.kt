package com.alexsheiko.party

import android.app.Application
import com.alexsheiko.party.util.dontCollectDebugInfo

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        dontCollectDebugInfo()
    }
}