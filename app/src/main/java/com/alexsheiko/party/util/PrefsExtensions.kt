package com.alexsheiko.party.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences

val Context.prefs: SharedPreferences
    get() = getDefaultSharedPreferences(this)