package com.flash.msc_light.component.feature

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.msc_light.utils.AppUtils
import com.flash.msc_light.utils.Constant
import com.flash.msc_light.utils.flash.FlashHelper
import com.flash.msc_light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingFlashAlertCloneMscViewModel @Inject constructor(@ApplicationContext private val context: Context): ViewModel() {

    @Inject
    lateinit var spManager: SpManager

    @Inject
    lateinit var flashHelper : FlashHelper

    val stateLive = MutableLiveData<Boolean>()
    val onTimeLive = MutableLiveData<Float>()
    val offTimeLive = MutableLiveData<Float>()
    val progressSbOnTimeLive = MutableLiveData<Int>()
    val progressSbOffTimeLive = MutableLiveData<Int>()
    val isTestingLive = MutableLiveData(false)

    fun getValuesSetting(type: String) {
        var onTime = 0L
        var offTime = 0L
        var state = false
        when(type){
            SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE -> {
                state = spManager.getTurnOnCall()
                onTime = spManager.getOnTimeFlashCallMS()
                offTime = spManager.getOffTimeFlashCallMS()
            }
            SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION -> {
                state = spManager.getTurnOnNotification()
                onTime = spManager.getOnTimeFlashNotificationMS()
                offTime = spManager.getOffTimeFlashNotificationMS()
                onTimeLive.postValue(onTime/1000f)
                offTimeLive.postValue(offTime/1000f)
            }
            SettingsFlashCloneMscAlertActivity.ALERT_SMS -> {
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



    fun saveOnTimePercent(type: String, p1: Int) {
        val onTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH.toFloat(), Constant.MAX_TIME_FLASH.toFloat())
        onTimeLive.postValue(onTime/1000f)
        when(type){
            SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setOnTimeFlashCall(onTime.toLong())
            }
            SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setOnTimeFlashNotification(onTime.toLong())
            }
            SettingsFlashCloneMscAlertActivity.ALERT_SMS -> {
                spManager.setOnTimeFlashSMS(onTime.toLong())
            }
        }
    } fun saveState(type: String, state: Boolean) {
        when(type){
            SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setTurnOnCall(state)
            }
            SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setTurnOnNotification(state)
            }
            SettingsFlashCloneMscAlertActivity.ALERT_SMS -> {
                spManager.setTurnOnSMS(state)
            }
        }
    }

    fun saveOffTimePercent(type: String, p1: Int) {
        val offTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH.toFloat(), Constant.MAX_TIME_FLASH.toFloat())
        offTimeLive.postValue(offTime/1000f)
        when(type){
            SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE -> {
                spManager.setOffTimeFlashCall(offTime.toLong())
            }
            SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION -> {
                spManager.setOffTimeFlashNotification(offTime.toLong())
            }
            SettingsFlashCloneMscAlertActivity.ALERT_SMS -> {
                spManager.setOffTimeFlashSMS(offTime.toLong())
            }
        }
    }

    fun testFlash(type: String) {
        var onTime = 0L
        var offTime = 0L
        when(type){
            SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE -> {
                onTime = spManager.getOnTimeFlashCallMS()
                offTime = spManager.getOffTimeFlashCallMS()
            }
            SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION -> {
                onTime = spManager.getOnTimeFlashNotificationMS()
                offTime = spManager.getOffTimeFlashNotificationMS()
            }
            SettingsFlashCloneMscAlertActivity.ALERT_SMS -> {
                onTime = spManager.getOnTimeFlashSMSMS()
                offTime = spManager.getOffTimeFlashSMSMS()
            }
        }

        isTestingLive.postValue(true)
        flashHelper.startNormal(context, onTime, offTime, true)
    }

    fun stopTest(){
        isTestingLive.postValue(false)
        flashHelper.stop()
    }

}