package com.flash.light.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.flash.light.R
import com.flash.light.domain.layer.FlashMode
import com.flash.light.domain.layer.LanguageModel
import com.flash.light.domain.layer.SoundModel
import com.flash.light.domain.layer.VibrationMode

class SpManager(private val preferences: SharedPreferences) {
    companion object {
        private var instance: SpManager? = null

        fun getInstance(context: Context): SpManager {
            if (instance == null) {
                instance = SpManager(PreferenceManager.getDefaultSharedPreferences(context))
            }
            return instance!!
        }
    }

    fun getInt(key: String, defaultValue: Int): Int = preferences.getInt(key, defaultValue)

    fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)


    fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    fun putLong(key: String, value: Long) {
        val editor = preferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String? =
        preferences.getString(key, defaultValue)

    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLanguage(languageModel: LanguageModel) {
        preferences.edit().putString(Constant.KEY_SP_CURRENT_LANGUAGE, languageModel.toJson())
            .apply()
    }

    fun getLanguage(): LanguageModel {
        return preferences.getString(Constant.KEY_SP_CURRENT_LANGUAGE, "")?.toLanguageModel()
            ?: LanguageModel("en", R.drawable.ic_english, R.string.english)
    }

    fun saveOnBoarding() {
        preferences.edit().putBoolean(Constant.KEY_SP_SHOW_ONBOARDING, false).apply()
    }

    fun getShowOnBoarding(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_SHOW_ONBOARDING, true)
    }

    fun setUMPShowed(showed: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_UMP_SHOWED, showed).apply()
    }
    fun isUMPShowed() : Boolean {
        return preferences.getBoolean(Constant.KEY_SP_UMP_SHOWED, false)
    }

    fun setSettingUMPShowing(b: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_UMP_SETTING_SHOWED, b).apply()
    }

    fun isSettingUMPShowing() : Boolean {
        return preferences.getBoolean(Constant.KEY_SP_UMP_SETTING_SHOWED, false)
    }

    fun setTurnOnTimeMs(second : Long){
        preferences.edit().putLong("turn_on_time_second", second).apply()
    }

    fun getTurnOnTimeMs() : Long{
        return preferences.getLong("turn_on_time_second", 100)
    }

    fun setTurnOffTimeMs(second : Long){
        preferences.edit().putLong("turn_off_time_second", second).apply()
    }

    fun getTurnOffTimeMs() : Long{
        return preferences.getLong("turn_off_time_second", 100)
    }
}