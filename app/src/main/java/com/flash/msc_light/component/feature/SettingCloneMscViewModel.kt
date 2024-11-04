package com.flash.msc_light.component.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.msc_light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingCloneMscViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var spManager: SpManager

    val flashWhenScreenOnStateLive = MutableLiveData(false)
    val flashWhenBatteryLowLive = MutableLiveData(false)
    val flashWhenNormalModeLive = MutableLiveData(false)
    val flashWhenVibrateModeLive = MutableLiveData(false)
    val flashWhenSilentModeLive = MutableLiveData(false)

    fun checkSettingValues(){
        flashWhenScreenOnStateLive.postValue(spManager.getNotFlashWhenScreenOn())
        flashWhenBatteryLowLive.postValue(spManager.getNotFlashWhenBatteryLow())
        flashWhenNormalModeLive.postValue(spManager.getFlashInNormalMode())
        flashWhenVibrateModeLive.postValue(spManager.getFlashInVibrateMode())
        flashWhenSilentModeLive.postValue(spManager.getFlashInSilentMode())
    }


    fun settingFlashWhenLowBattery(state : Boolean){
        spManager.setNotFlashWhenBatteryLow(state)
    }

    fun settingFlashInNormalMode(state: Boolean){
        spManager.setFlashInNormalMode(state)
    }

    fun settingFlashWhenScreenOn(state : Boolean){
        spManager.setNotFlashWhenScreenOn(state)
    }
    fun settingFlashInVibrateMode(state: Boolean){
        spManager.setFlashInVibrateMode(state)
    }

    fun settingFlashInSilent(state: Boolean){
        spManager.setFlashInSilentMode(state)
    }
}