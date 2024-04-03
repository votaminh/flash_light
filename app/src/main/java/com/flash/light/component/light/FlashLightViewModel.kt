package com.flash.light.component.light

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.utils.AppUtils
import com.flash.light.utils.Constant
import com.flash.light.utils.FlashHelper
import com.flash.light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FlashLightViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    @Inject
    lateinit var spManager : SpManager
    private val flashHelper = FlashHelper()

    val isFlashTurnOn = MutableLiveData(false)
    val onTimeLive = MutableLiveData<Float>()
    val offTimeLive = MutableLiveData<Float>()
    val progressSbOnTimeLive = MutableLiveData<Int>()
    val progressSbOffTimeLive = MutableLiveData<Int>()

    fun getValuesSetting() {
        var onTime = 0L
        var offTime = 0L

        onTime = spManager.getOnTimeFlashLightMS()
        offTime = spManager.getOffTimeFlashLightMS()

        onTimeLive.postValue(onTime/1000f)
        offTimeLive.postValue(offTime/1000f)
        progressSbOnTimeLive.postValue(AppUtils.invertRange(onTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
        progressSbOffTimeLive.postValue(AppUtils.invertRange(offTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
    }

    fun startFlash(){
        val timeTurnOn = spManager.getOnTimeFlashLightMS()
        val timeTurnOff = spManager.getOffTimeFlashLightMS()
        flashHelper.start(context, timeTurnOn, timeTurnOff)
        isFlashTurnOn.postValue(true)
    }

    fun stopFlash(){
        isFlashTurnOn.postValue(false)
        flashHelper.stop()
    }

    fun saveOnTimePercent(p1: Int) {
        val onTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toLong()
        spManager.setOnTimeFlashLight(onTime)
        onTimeLive.postValue(onTime/1000f)
    }

    fun saveOffTimePercent(p1: Int) {
        val offTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toLong()
        spManager.setOffTimeFlashLight(offTime)
        offTimeLive.postValue(offTime/1000f)
    }
}