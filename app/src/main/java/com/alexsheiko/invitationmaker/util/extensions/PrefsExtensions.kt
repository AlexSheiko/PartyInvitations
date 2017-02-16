package com.alexsheiko.invitationmaker.util.extensions

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

interface PrefsExtensions {

    val Context.prefs: SharedPreferences get() = PreferenceManager.getDefaultSharedPreferences(this)
}