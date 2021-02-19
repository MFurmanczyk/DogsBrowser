package com.example.dogsbrowser.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class SharedPreferencesHelper {


    companion object {

        private const val CACHE_TIME = "PrefTime"

        private var preferences: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }

    }

    fun saveCacheTime(time: Long) {
        preferences?.edit(commit = true) {
            putLong(CACHE_TIME, time)
        }
    }

    fun getCacheTime() = preferences?.getLong(CACHE_TIME, 0L)
}