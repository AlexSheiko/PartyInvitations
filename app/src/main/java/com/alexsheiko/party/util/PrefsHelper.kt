package com.alexsheiko.party.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

val Context.prefs: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.isUserPro(): Boolean {
    return prefs.getBoolean("isPro", false)
}

fun Context.saveUserPro() {
    prefs.edit().putBoolean("isPro", true).commit()
}