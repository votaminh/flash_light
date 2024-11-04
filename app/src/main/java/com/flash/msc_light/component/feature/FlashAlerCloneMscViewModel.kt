package com.flash.msc_light.component.feature

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.msc_light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlashAlerCloneMscViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var spManager: SpManager

    val stateLive = MutableLiveData(false)

    val flashWhenNormalModeLive = MutableLiveData(false)
    val flashWhenVibrateModeLive = MutableLiveData(false)
    val flashWhenSilentModeLive = MutableLiveData(false)

    fun saveStateFlash(state : Boolean) {
        spManager.setStateFlash(state)
        stateLive.postValue(state)
    }
    fun checkState(){
        stateLive.postValue(spManager.getStateFlash())
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