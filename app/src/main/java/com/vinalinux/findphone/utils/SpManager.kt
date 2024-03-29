package com.vinalinux.findphone.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.vinalinux.findphone.R
import com.vinalinux.findphone.domain.layer.FlashMode
import com.vinalinux.findphone.domain.layer.LanguageModel
import com.vinalinux.findphone.domain.layer.SoundModel
import com.vinalinux.findphone.domain.layer.VibrationMode

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

    fun saveSoundModel(soundModel: SoundModel) {
        preferences.edit().putString(Constant.KEY_SP_CURRENT_SOUND_MODEL, soundModel.toJson())
            .apply()
    }

    fun getSoundModel(): SoundModel {
        return preferences.getString(Constant.KEY_SP_CURRENT_SOUND_MODEL, "")?.toSoundModel()
            ?: SoundModel(R.drawable.img_police, R.string.txt_police, R.raw.sound_police)
    }

    fun saveDuration(duration: Int) {
        preferences.edit().putInt(Constant.KEY_SP_CURRENT_DURATION, duration).apply()
    }

    fun getDuration(): Int {
        return preferences.getInt(Constant.KEY_SP_CURRENT_DURATION, 15 * 1000)
    }

    fun saveFlash(flash: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_CURRENT_FLASH, flash).apply()
    }

    fun getFlash(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_CURRENT_FLASH, true)
    }

    fun saveFlashMode(flashMode: FlashMode) {
        preferences.edit().putInt(Constant.KEY_SP_CURRENT_FLASH_MODE, flashMode.ordinal).apply()
    }

    fun getFlashMode(): FlashMode {
        return enumValues<FlashMode>()[preferences.getInt(Constant.KEY_SP_CURRENT_FLASH_MODE, 0)]
    }

    fun saveVibrationMode(vibrationMode: VibrationMode) {
        preferences.edit().putInt(Constant.KEY_SP_CURRENT_VIBRATION_MODE, vibrationMode.ordinal)
            .apply()
    }

    fun getVibrationMode(): VibrationMode {
        return enumValues<VibrationMode>()[preferences.getInt(
            Constant.KEY_SP_CURRENT_VIBRATION_MODE,
            0
        )]
    }

    fun saveVibration(vibration: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_CURRENT_VIBRATION, vibration).apply()
    }

    fun getVibration(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_CURRENT_VIBRATION, true)
    }

    fun saveEnableVolume(enable: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_CURRENT_ENABLE_VOLUME, enable).apply()
    }

    fun getEnableVolume(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_CURRENT_ENABLE_VOLUME, true)
    }

    fun saveVolume(volume: Int) {
        preferences.edit().putInt(Constant.KEY_SP_CURRENT_VOLUME, volume).apply()
    }

    fun getVolume(): Int {
        return preferences.getInt(Constant.KEY_SP_CURRENT_VOLUME, 100)
    }

    fun saveSensitivity(progress: Int) {
        preferences.edit().putInt(Constant.KEY_SP_CURRENT_SENSITIVITY, progress).apply()
    }

    fun getSensitivity(): Int {
        return preferences.getInt(Constant.KEY_SP_CURRENT_SENSITIVITY, 100)
    }

    fun saveLanguage(languageModel: LanguageModel) {
        preferences.edit().putString(Constant.KEY_SP_CURRENT_LANGUAGE, languageModel.toJson())
            .apply()
    }

    fun getLanguage(): LanguageModel {
        return preferences.getString(Constant.KEY_SP_CURRENT_LANGUAGE, "")?.toLanguageModel()
            ?: LanguageModel("en", R.drawable.ic_english, R.string.english)
    }

    fun saveWakeup(wakeUp: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_CURRENT_WAKEUP, wakeUp).apply()
    }

    fun getWakeup(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_CURRENT_WAKEUP, false)
    }

    fun saveOnBoarding() {
        preferences.edit().putBoolean(Constant.KEY_SP_SHOW_ONBOARDING, false).apply()
    }

    fun getShowOnBoarding(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_SHOW_ONBOARDING, true)
    }

    fun saveRunningAudioClassifier(isRunning: Boolean) {
        preferences.edit().putBoolean(Constant.KEY_SP_RUNNING_AUDIO_CLASSIFIER, isRunning).apply()
    }

    fun isRunningAudioClassifier(): Boolean {
        return preferences.getBoolean(Constant.KEY_SP_RUNNING_AUDIO_CLASSIFIER, false)
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
}