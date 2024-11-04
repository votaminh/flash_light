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


    fun saveStateFlash(state : Boolean) {
        spManager.setStateFlash(state)
        stateLive.postValue(state)
    }
    fun checkState(){
        stateLive.postValue(spManager.getStateFlash())
    }
}