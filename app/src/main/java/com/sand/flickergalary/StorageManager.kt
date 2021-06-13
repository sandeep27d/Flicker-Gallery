package com.sand.flickergalary

import android.content.Context
import com.google.gson.Gson

object StorageManager {
    private val KEY: String = javaClass.simpleName

    private val pref = FlickerApp.instance.getSharedPreferences(KEY, Context.MODE_PRIVATE)

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun putString(key: String, value: String?) {
        return pref.edit().putString(key, value).apply()
    }
}