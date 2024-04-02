package com.flash.light.component.setting_alert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.light.utils.AppUtils
import com.flash.light.utils.Constant
import com.flash.light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingFlashAlertViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var spManager: SpManager

    val stateLive = MutableLiveData<Boolean>()
    val onTimeLive = MutableLiveData<Float>()
    val offTimeLive = MutableLiveData<Float>()
    val progressSbOnTimeLive = MutableLiveData<Int>()
    val progressSbOffTimeLive = MutableLiveData<Int>()

    fun getValuesSetting(type: String) {
        var onTime = 0L
        var offTime = 0L
        var state = false
        when(type){
            SettingsFlashAlertActivity.ALERT_CALL_PHONE -> {
                state = spManager.getTurnOnCall()
                onTime = spManager.getOnTimeFlashCallMS()
                offTime = spManager.getOffTimeFlashCallMS()
            }
            SettingsFlashAlertActivity.ALERT_NOTIFICATION -> {
                state = spManager.getTurnOnNotification()
                onTime = spManager.getOnTimeFlashNotificationMS()
                offTime = spManager.getOffTimeFlashNotificationMS()
                onTimeLive.postValue(onTime/1000f)
                offTimeLive.postValue(offTime/1000f)
            }
            SettingsFlashAlertActivity.ALERT_SMS -> {
                state = spManager.getTurnOnSMS()
                onTime = spManager.getOnTimeFlashSMSMS()
                offTime = spManager.getOffTimeFlashSMSMS()
            }
        }

        stateLive.postValue(state)
        onTimeLive.postValue(onTime/1000f)
        offTimeLive.postValue(offTime/1000f)
        progressSbOnTimeLive.postValue(AppUtils.invertRange(onTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
        progressSbOffTimeLive.postValue(AppUtils.invertRange(offTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
    }

    fun saveState(type: String, state: Boolean) {
        when(type){
            SettingsFlashAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setTurnOnCall(state)
            }
            SettingsFlashAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setTurnOnNotification(state)
            }
            SettingsFlashAlertActivity.ALERT_SMS -> {
                spManager.setTurnOnSMS(state)
            }
        }
    }

    fun saveOnTimePercent(type: String, p1: Int) {
        val onTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH.toFloat(), Constant.MAX_TIME_FLASH.toFloat())
        onTimeLive.postValue(onTime/1000f)
        when(type){
            SettingsFlashAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setOnTimeFlashCall(onTime.toLong())
            }
            SettingsFlashAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setOnTimeFlashNotification(onTime.toLong())
            }
            SettingsFlashAlertActivity.ALERT_SMS -> {
                spManager.setOnTimeFlashSMS(onTime.toLong())
            }
        }
    }

    fun saveOffTimePercent(type: String, p1: Int) {
        val offTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH.toFloat(), Constant.MAX_TIME_FLASH.toFloat())
        offTimeLive.postValue(offTime/1000f)
        when(type){
            SettingsFlashAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setOffTimeFlashCall(offTime.toLong())
            }
            SettingsFlashAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setOffTimeFlashNotification(offTime.toLong())
            }
            SettingsFlashAlertActivity.ALERT_SMS -> {
                spManager.setOffTimeFlashSMS(offTime.toLong())
            }
        }
    }

}