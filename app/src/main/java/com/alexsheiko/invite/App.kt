package com.alexsheiko.invite

import android.app.Application
import com.alexsheiko.invite.util.initCustomFont

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initCustomFont()
    }
}