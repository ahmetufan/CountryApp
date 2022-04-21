package com.example.kotlincountryap.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {

    companion object {

        private val PREFERENCES_TIME="preferences_time"

        private var shraedPreferences: SharedPreferences? = null

        @Volatile
        private var instance: CustomSharedPreferences? = null

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            shraedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }

        operator fun invoke(context: Context): CustomSharedPreferences =
            instance ?: synchronized(lock) {
                instance ?: makeCustomSharedPreferences(context).also {
                    instance = it
                }
            }

        private val lock = Any()



    }
    fun saveTime(time:Long) {
        shraedPreferences?.edit(commit=true) {
            putLong(PREFERENCES_TIME,time)
        }
    }

    fun getTime() = shraedPreferences?.getLong(PREFERENCES_TIME,0)
}