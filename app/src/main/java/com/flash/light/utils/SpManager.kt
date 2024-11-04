package com.flash.light.utils

import android.content.Context
import android.content.SharedPreferences
import com.flash.light.R
import com.flash.light.domain.layer.LanguageModel

class SpManager(private val preferences: SharedPreferences) {
    companion object {
        private var instance: SpManager? = null

        fun getInstance(context: Context): SpManager {
            if (instance == null) {
                instance = SpManager(context.getSharedPreferences("flashlight",Context.MODE_PRIVATE))
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
            ?: LanguageModel("en", R.drawable.ic_english_clone_msc, R.string.english)
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

    fun setTurnOnCall(turnOn : Boolean){
        preferences.edit().putBoolean("turn_on_incoming_calls", turnOn).apply()
    }

    fun getTurnOnCall() : Boolean{
        return preferences.getBoolean("turn_on_incoming_calls", false)
    }

    fun setOnTimeFlashCall(onTime : Long){
        preferences.edit().putLong("on_time_flash_call", onTime).apply()
    }

    fun getOnTimeFlashCallMS() : Long{
        return preferences.getLong("on_time_flash_call", 100)
    }

    fun setOffTimeFlashCall(onTime : Long){
        preferences.edit().putLong("off_time_flash_call", onTime).apply()
    }

    fun getOffTimeFlashCallMS() : Long{
        return preferences.getLong("off_time_flash_call", 100)
    }

    fun setTurnOnNotification(turnOn : Boolean){
        preferences.edit().putBoolean("turn_on_notification", turnOn).apply()
    }

    fun getTurnOnNotification() : Boolean{
        return preferences.getBoolean("turn_on_notification", false)
    }

    fun setOnTimeFlashNotification(onTime : Long){
        preferences.edit().putLong("on_time_Notification", onTime).apply()
    }

    fun getOnTimeFlashNotificationMS() : Long{
        return preferences.getLong("on_time_Notification", 100)
    }

    fun setOffTimeFlashNotification(onTime : Long){
        preferences.edit().putLong("off_time_Notification", onTime).apply()
    }

    fun getOffTimeFlashNotificationMS() : Long{
        return preferences.getLong("off_time_Notification", 100)
    }

    fun setTurnOnSMS(turnOn : Boolean){
        preferences.edit().putBoolean("turn_on_SMS", turnOn).apply()
    }

    fun getTurnOnSMS() : Boolean{
        return preferences.getBoolean("turn_on_SMS", false)
    }

    fun setOnTimeFlashSMS(onTime : Long){
        preferences.edit().putLong("on_time_SMS", onTime).apply()
    }

    fun getOnTimeFlashSMSMS() : Long{
        return preferences.getLong("on_time_SMS", 100)
    }

    fun setOffTimeFlashSMS(onTime : Long){
        preferences.edit().putLong("off_time_SMS", onTime).apply()
    }

    fun getOffTimeFlashSMSMS() : Long{
        return preferences.getLong("off_time_SMS", 100)
    }

    fun setOnTimeFlashLight(onTime : Long){
        preferences.edit().putLong("on_time_Light", onTime).apply()
    }

    fun getOnTimeFlashLightMS() : Long{
        return preferences.getLong("on_time_Light", 100)
    }

    fun setOffTimeFlashLight(onTime : Long){
        preferences.edit().putLong("off_time_Light", onTime).apply()
    }

    fun getOffTimeFlashLightMS() : Long{
        return preferences.getLong("off_time_Light", 100)
    }

    fun setStateFlash(state : Boolean){
        preferences.edit().putBoolean("sate_flash_notification", state).apply()
    }

    fun getStateFlash() : Boolean{
        return preferences.getBoolean("sate_flash_notification", false)
    }

    fun setNotFlashWhenScreenOn(state: Boolean){
        preferences.edit().putBoolean("FlashWhenScreenOn", state).apply()
    }

    fun getNotFlashWhenScreenOn() : Boolean{
        return preferences.getBoolean("FlashWhenScreenOn", true)
    }

    fun setNotFlashWhenBatteryLow(state: Boolean){
        preferences.edit().putBoolean("FlashWhenBatteryLow", state).apply()
    }

    fun getNotFlashWhenBatteryLow() : Boolean{
        return preferences.getBoolean("FlashWhenBatteryLow", true)
    }

    fun setFlashInNormalMode(state: Boolean){
        preferences.edit().putBoolean("FlashInNormalMode", state).apply()
    }

    fun getFlashInNormalMode() : Boolean{
        return preferences.getBoolean("FlashInNormalMode", true)
    }

    fun setFlashInVibrateMode(state: Boolean){
        preferences.edit().putBoolean("FlashInVibrateMode", state).apply()
    }

    fun getFlashInVibrateMode() : Boolean{
        return preferences.getBoolean("FlashInVibrateMode", true)
    }

    fun setFlashInSilentMode(state: Boolean){
        preferences.edit().putBoolean("FlashInSilentMode", state).apply()
    }

    fun getFlashInSilentMode() : Boolean{
        return preferences.getBoolean("FlashInSilentMode", true)
    }
}