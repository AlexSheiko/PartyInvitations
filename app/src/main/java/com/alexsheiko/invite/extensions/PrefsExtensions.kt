package com.alexsheiko.invite.extensions

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences

interface PrefsExtensions {

    val Context.prefs: SharedPreferences
        get() = getDefaultSharedPreferences(this)

}