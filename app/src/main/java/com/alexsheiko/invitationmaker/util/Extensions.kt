package com.alexsheiko.invitationmaker.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

interface Extensions {

    val Context.prefs: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(this)
}