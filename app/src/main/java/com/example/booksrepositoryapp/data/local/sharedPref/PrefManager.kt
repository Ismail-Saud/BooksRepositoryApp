package com.example.booksrepositoryapp.data.local.sharedPref

import android.content.Context
import androidx.core.content.edit

object PrefManager {
    private const val PREF_NAME = "app_prefs"

    private fun prefs(context: Context) = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveJson(context: Context, key: String, json: String) {
        prefs(context).edit {
            putString(key, json)
        }
    }
    fun getJson(context: Context, key: String): String? {
        return prefs(context).getString(key, null)
    }
    fun remove(context: Context, key: String) {
        prefs(context).edit {
            remove(key)
        }
    }

    fun saveBoolean(
        context: Context,
        key: String,
        value: Boolean
    ) {
        prefs(context).edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(
        context: Context,
        key: String,
        defaultValue: Boolean = false
    ): Boolean {
        return prefs(context).getBoolean(key, defaultValue)
    }
}