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
class FlashLightCloneMscViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    @Inject
    lateinit var spManager : SpManager
    @Inject
    lateinit var flashHelper : FlashHelper

    val isFlashTurnOn = MutableLiveData(false)
    val onTimeLive = MutableLiveData<Float>()
    val offTimeLive = MutableLiveData<Float>()
    val progressSbOnTimeLive = MutableLiveData<Int>()
    val progressSbOffTimeLive = MutableLiveData<Int>()


    fun startFlash(){
        val timeTurnOn = spManager.getOnTimeFlashLightMS()
        val timeTurnOff = spManager.getOffTimeFlashLightMS()
        flashHelper.startNormal(context, timeTurnOn, timeTurnOff, true)
        isFlashTurnOn.postValue(true)
    }

    fun saveOnTimePercent(p1: Int) {
        val onTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toLong()
        spManager.setOnTimeFlashLight(onTime)
        onTimeLive.postValue(onTime/1000f)
    }fun getValuesSetting() {
        var onTime = 0L
        var offTime = 0L

        onTime = spManager.getOnTimeFlashLightMS()
        offTime = spManager.getOffTimeFlashLightMS()

        onTimeLive.postValue(onTime/1000f)
        offTimeLive.postValue(offTime/1000f)
        progressSbOnTimeLive.postValue(AppUtils.invertRange(onTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
        progressSbOffTimeLive.postValue(AppUtils.invertRange(offTime.toFloat(), Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toInt())
    }


    fun stopFlash(){
        isFlashTurnOn.postValue(false)
        flashHelper.stop()
    }
    fun saveOffTimePercent(p1: Int) {
        val offTime = AppUtils.range(p1, Constant.MIN_TIME_FLASH, Constant.MAX_TIME_FLASH).toLong()
        spManager.setOffTimeFlashLight(offTime)
        offTimeLive.postValue(offTime/1000f)
    }
}