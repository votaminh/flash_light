package com.flash.light.utils

import androidx.preference.PreferenceManager
import com.flash.light.App

class CommonSharedPreferences {
    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(App.instance!!.applicationContext)

    companion object {
        private val INSTANCE = CommonSharedPreferences()

        @Synchronized
        fun getInstance() = INSTANCE
    }

    fun getInt(key: String, defaultValue: Int): Int = sharedPreferences.getInt(key, defaultValue)

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)


    fun getLong(key: String, defaultValue: Long): Long =
        sharedPreferences.getLong(key, defaultValue)

    fun putLong(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String? =
        sharedPreferences.getString(key, defaultValue)

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}