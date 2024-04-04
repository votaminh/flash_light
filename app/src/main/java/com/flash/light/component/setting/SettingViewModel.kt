package com.flash.light.component.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var spManager: SpManager

    val flashWhenScreenOnStateLive = MutableLiveData(false)
    val flashWhenBatteryLowLive = MutableLiveData(false)
    val flashWhenNormalModeLive = MutableLiveData(false)
    val flashWhenVibrateModeLive = MutableLiveData(false)
    val flashWhenSilentModeLive = MutableLiveData(false)

    fun checkSettingValues(){
        flashWhenScreenOnStateLive.postValue(spManager.getFlashWhenScreenOn())
        flashWhenBatteryLowLive.postValue(spManager.getFlashWhenBatteryLow())
        flashWhenNormalModeLive.postValue(spManager.getFlashInNormalMode())
        flashWhenVibrateModeLive.postValue(spManager.getFlashInVibrateMode())
        flashWhenSilentModeLive.postValue(spManager.getFlashInSilentMode())
    }

    fun settingFlashWhenScreenOn(state : Boolean){
        spManager.setFlashWhenScreenOn(state)
    }

    fun settingFlashWhenLowBattery(state : Boolean){
        spManager.setFlashWhenBatteryLow(state)
    }

    fun settingFlashInNormalMode(state: Boolean){
        spManager.setFlashInNormalMode(state)
    }

    fun settingFlashInVibrateMode(state: Boolean){
        spManager.setFlashInVibrateMode(state)
    }

    fun settingFlashInSilent(state: Boolean){
        spManager.setFlashInSilentMode(state)
    }
}