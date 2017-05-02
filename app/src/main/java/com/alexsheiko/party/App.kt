package com.alexsheiko.party

import android.app.Application
import com.alexsheiko.party.util.initCustomFont

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initCustomFont()
    }
}